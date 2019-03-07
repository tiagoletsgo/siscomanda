package br.com.siscomanda.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import br.com.siscomanda.base.service.VendaService;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Adicional;
import br.com.siscomanda.model.Bandeira;
import br.com.siscomanda.model.FormaPagamento;
import br.com.siscomanda.model.ItemVenda;
import br.com.siscomanda.model.PagamentoVenda;
import br.com.siscomanda.model.Venda;
import br.com.siscomanda.repository.dao.BandeiraDAO;
import br.com.siscomanda.repository.dao.FormaPagamentoDAO;
import br.com.siscomanda.repository.dao.VendaDAO;

public class FechaContaService extends VendaService implements Serializable {

	private static final long serialVersionUID = -3622747048753270872L;
	
	@Inject
	private VendaDAO vendaDAO;
	
	@Inject
	private FormaPagamentoDAO formaPagamentoDAO;
	
	@Inject
	private BandeiraDAO bandeiraDAO;
	
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
		Long id = pagamentos.isEmpty() ? 1L : pagamentos.get(pagamentos.size() -1).getId() + 1;
		
		PagamentoVenda pagamentoClone = new PagamentoVenda();
		pagamentoClone.setId(id);
		pagamentoClone.setBandeira(pagamento.getBandeira());
		pagamentoClone.setValorRecebido(pagamento.getValorRecebido());
		pagamentoClone.setFormaPagamento(pagamento.getFormaPagamento());
		
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
		
		return pagamentoClone;
	}
	
	public Double calculaTroco(Double valorPago, Double valorTotal, FormaPagamento formaPagamento, boolean estorno) {
		Double total = BigDecimal.ZERO.doubleValue();
		if(valorPago > valorTotal && formaPagamento.getDescricao().equals("DINHEIRO") && !estorno) {
			total = valorPago - valorTotal;
		}
		else if(formaPagamento.getDescricao().equals("DINHEIRO") && estorno) {
			total = valorTotal - valorPago;
		}
		return total;
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
		List<Venda> vendas = vendaDAO.buscaPor(venda);
		
		if(vendas.isEmpty()) {
			throw new SiscomandaException("Registro não encontrado.");
		}
		
		return vendas.get(0);
	}
	
	public List<Adicional> carregaAdicionais(ItemVenda item) {
		return vendaDAO.buscaAdicionalVenda(item);
	}
}
