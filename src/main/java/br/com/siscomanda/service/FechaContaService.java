package br.com.siscomanda.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import br.com.siscomanda.base.service.VendaService;
import br.com.siscomanda.config.jpa.Transactional;
import br.com.siscomanda.enumeration.EStatus;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.interfaces.Calculadora;
import br.com.siscomanda.model.Adicional;
import br.com.siscomanda.model.Bandeira;
import br.com.siscomanda.model.Caixa;
import br.com.siscomanda.model.FormaPagamento;
import br.com.siscomanda.model.ItemVenda;
import br.com.siscomanda.model.PagamentoVenda;
import br.com.siscomanda.model.Venda;
import br.com.siscomanda.repository.dao.BandeiraDAO;
import br.com.siscomanda.repository.dao.CaixaDAO;
import br.com.siscomanda.repository.dao.FormaPagamentoDAO;
import br.com.siscomanda.repository.dao.VendaDAO;
import br.com.siscomanda.util.JSFUtil;

public class FechaContaService extends VendaService implements Serializable {

	private static final long serialVersionUID = -3622747048753270872L;
	
	@Inject
	private VendaDAO vendaDAO;
	
	@Inject
	private FormaPagamentoDAO formaPagamentoDAO;
	
	@Inject
	private BandeiraDAO bandeiraDAO;
	
	@Inject
	private CaixaDAO caixaDAO;
	
	public PagamentoVenda carregaPagamento(Venda venda) {
		
		Double troco = new Double(0);
		if(!venda.getPagamentos().isEmpty()) {
			for(PagamentoVenda pagamento : venda.getPagamentos()) {
				if(pagamento.getValorTroco() > new Double(0)) {
					troco = pagamento.getValorTroco();
				}
			}
		}
		
		PagamentoVenda pagamento = new PagamentoVenda();
		pagamento.setValorTroco(new Double(0));
		pagamento.setValorPago(venda.getValorPago() == null ? new Double(0) : venda.getValorPago());
		pagamento.setDesconto(venda.getDesconto() == null ? new Double(0) : venda.getDesconto());
		pagamento.setTaxaEntrega(venda.getTaxaEntrega() == null ? new Double(0) : venda.getTaxaEntrega());
		pagamento.setTaxaServico(venda.getTaxaServico() == null ? new Double(0) : venda.getTaxaServico());
		pagamento.setValorTotal(venda.getTotal());
		pagamento.setValorVenda(venda.getSubtotal());
		pagamento.setValorTroco(troco);
		
		return pagamento;
	}
	
	public void limpaId(List<PagamentoVenda> pagamentos) {
		for(PagamentoVenda pagamento : pagamentos) {
			pagamento.setId(null);
		}
	}
	
	@Transactional
	public Venda salvar(Venda venda, PagamentoVenda pagamento) throws SiscomandaException {
		Caixa caixa = caixaDAO.temCaixaAberto();
		
		if(caixa == null) {
			throw new SiscomandaException("Não foi realizado a abertura do caixa.");
		}
		
		if(venda.getPagamentos().isEmpty()) {
			throw new SiscomandaException("É necessário informar pelo menos uma forma de pagamento antes de salvar.");
		}
		
		if(pagamento.getValorPago() < BigDecimal.ZERO.doubleValue() || venda.getTotal() < BigDecimal.ZERO.doubleValue()) {
			throw new SiscomandaException("Não é possivel salvar pagamento com valores negativos.");
		}
		
		if(venda.getStatus().equals(EStatus.CANCELADO)) {
			throw new SiscomandaException("Pagamento com status cancelado não pode ser alterado.");
		}
		
		if(venda.getStatus().equals(EStatus.PAGO)) {
			throw new SiscomandaException("Pagamento com status pago não pode ser alterado.");
		}
		
		venda.setTotal(pagamento.getValorTotal());
		venda.setValorPago(pagamento.getValorPago());
		venda.setStatus(statusPagamento(pagamento.getValorPago(), pagamento.getValorTotal()));
		venda.setPago(isPago(venda));
		venda.setCaixa(caixa);
		
		for(PagamentoVenda pag : venda.getPagamentos()) {
			pag.setDataPagamento(new Date());
			pag.setCaixa(venda.getCaixa());
		}

		venda = vendaDAO.salvar(venda);
		
		if(venda.isPago()) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Pagamento fechado com sucesso.");
		}
		
		if(venda.isNotPago()) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_WARN, "Pagamento salvo com valor parcial.");
		}
				
		return venda;
	}
	
	private EStatus statusPagamento(Double valorPago, Double valorTotal) {
		EStatus status = EStatus.EM_ABERTO;		
		if(valorPago >= valorTotal) {
			status = EStatus.PAGO;
		}
		else if(valorPago < valorTotal) {
			status = EStatus.PAGO_PARCIAL;
		}
		return status;
	}
	
	private boolean isPago(Venda venda) {
		if(venda.getStatus().equals(EStatus.PAGO)) {
			return true;
		}
		return false;
	}
	
	public FormaPagamento buscaFormaPagamentoPorDescricao(String descricao) throws SiscomandaException {
		try {			
			return formaPagamentoDAO.porDescricao(descricao, FormaPagamento.class).get(0);
		}
		catch(SiscomandaException e) {
			throw new SiscomandaException("Forma de pagamento não encontrada.");
		}
	}
	
	public List<Bandeira> buscaBandeiraPorFormaPagamentoVinculada(FormaPagamento formaPagamento) {
		return bandeiraDAO.porFormaPagamento(formaPagamento, true);
	}
	
	public PagamentoVenda incluiPagamento(PagamentoVenda pagamento, List<PagamentoVenda> pagamentos, Double valorFaltante) throws SiscomandaException {
		
		if(pagamento.getValorRecebido() == null || pagamento.getValorRecebido().equals(new Double(0))) {
			throw new SiscomandaException("Informe ou selecione um valor para incluir pagamento.");
		}
		
		if(pagamento.getFormaPagamento().getDescricao().equals("DEBITO") || pagamento.getFormaPagamento().getDescricao().equals("CREDITO")) {
			if(pagamento.getBandeira() == null) {				
				throw new SiscomandaException("Forma de pagamento crédito ou débito deve ser informada uma bandeira.");
			}
		}
		
		if(pagamento.getFormaPagamento().getDescricao().equals("DEBITO") && pagamento.getValorRecebido() > valorFaltante
				|| pagamento.getFormaPagamento().getDescricao().equals("CREDITO") && pagamento.getValorRecebido() > valorFaltante) {
			
			throw new SiscomandaException("Forma de pagamento crédito ou débito não permite troco.");
		}
		
		Long id = pagamentos.isEmpty() ? 1L : pagamentos.get(pagamentos.size() -1).getId() + 1;
		
		PagamentoVenda pagamentoClone = new PagamentoVenda();
		pagamentoClone.setId(id);
		pagamentoClone.setBandeira(pagamento.getBandeira());
		pagamentoClone.setValorRecebido(pagamento.getValorRecebido());
		pagamentoClone.setFormaPagamento(pagamento.getFormaPagamento());
		
		pagamentoClone.setValorPago(pagamento.getValorPago());
		pagamentoClone.setValorTroco(pagamento.getValorTroco());
		pagamentoClone.setDesconto(pagamento.getDesconto());
		pagamentoClone.setTaxaEntrega(pagamento.getTaxaEntrega());
		pagamentoClone.setTaxaServico(pagamento.getTaxaServico());
		pagamentoClone.setValorTotal(pagamento.getValorTotal());
		pagamentoClone.setValorVenda(pagamento.getValorVenda());
		
		return pagamentoClone;
	}
	
	public Double calculaFaltaPagar(Calculadora calculadora, Double total, Double desconto, Double acrescimo) {
		try {
			return calculadora.aplicaCalculo(total, desconto, acrescimo);
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			return new Double(0);
		}
	}
	
	public Double calculaTroco(Double valorPago, Double valorTotal, FormaPagamento formaPagamento) {
		Double total = BigDecimal.ZERO.doubleValue();
		if(valorPago > valorTotal && formaPagamento.getDescricao().equals("DINHEIRO")) {
			total = valorPago - valorTotal;
		}
		
		return total;
	}
	
	public Double estornarFormaPagamento(PagamentoVenda pagamento, FormaPagamento formaPagamento, Double valorFaltante) {
		Double valorEstornado = pagamento.getValorPago() != 0.0 ? pagamento.getValorTroco() : 0.0;
		if(formaPagamento.getDescricao().equals("DINHEIRO")) {
			if(pagamento.getValorTotal() > pagamento.getValorPago()) {
				valorEstornado = pagamento.getValorTotal() - valorFaltante;			
			}
		}
		
		if(valorFaltante > 0) {
			valorEstornado = 0.0;
		}
		
		return valorEstornado;
	}
	
	public Double calculaTotalPago(List<PagamentoVenda> pagamentos) {
		Double total = BigDecimal.ZERO.doubleValue();
		for(PagamentoVenda pagamento : pagamentos) {
			total += pagamento.getValorRecebido();
		}
		return total;
	}
	
	public Double calculaValorFantante(Double valorTotal, Double valorPago) {
		Double total = BigDecimal.ZERO.doubleValue();
		total = (valorTotal - valorPago);
		if(total < BigDecimal.ZERO.doubleValue()) {
			total = BigDecimal.ZERO.doubleValue();
		}
		
		return total;
	}
	
	public Venda buscaVenda(Venda venda) throws SiscomandaException {
		List<Venda> vendas = vendaDAO.buscaPor(venda, true);
		
		if(vendas.isEmpty()) {
			throw new SiscomandaException("Registro não encontrado.");
		}
		
		return vendas.get(0);
	}
	
	public List<Adicional> carregaAdicionais(ItemVenda item) {
		return vendaDAO.buscaAdicionalVenda(item);
	}
}
