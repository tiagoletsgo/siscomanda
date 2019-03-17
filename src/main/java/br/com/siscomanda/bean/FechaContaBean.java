package br.com.siscomanda.bean;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.siscomanda.base.bean.BaseBean;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Adicional;
import br.com.siscomanda.model.Bandeira;
import br.com.siscomanda.model.FormaPagamento;
import br.com.siscomanda.model.ItemVenda;
import br.com.siscomanda.model.PagamentoVenda;
import br.com.siscomanda.model.Venda;
import br.com.siscomanda.service.AplicaDescontoService;
import br.com.siscomanda.service.AplicaTaxaEntregaService;
import br.com.siscomanda.service.FechaContaService;
import br.com.siscomanda.util.JSFUtil;
import br.com.siscomanda.util.StringUtil;

@Named
@ViewScoped
public class FechaContaBean extends BaseBean<Venda> implements Serializable {

	private static final long serialVersionUID = 3299850942772424169L;
	
	@Inject
	private FechaContaService service;
	
	private List<Bandeira> bandeiras;
	
	private PagamentoVenda pagamento;
	
	private PagamentoVenda pagamentoSelecionado;
	
	private Long codigo;
	
	private Double valorFaltante;
	
	private Double totalPagar;
	
	private String acao;
	
	@Override
	protected void init() {
		if(!FacesContext.getCurrentInstance().isPostback()) {	
			
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext external = context.getExternalContext();
			
			codigo = Long.parseLong(external.getRequestParameterMap().get("codigo"));
			setEntity(new Venda(codigo));
			
			buscaVenda();
		}
	}
	
	private void buscaVenda() {
		try {			
			setEntity(service.buscaVenda(getEntity()));
			pagamento = service.carregaPagamento(getEntity());						
			valorFaltante = service.calculaValorFantante(pagamento.getValorTotal(), pagamento.getValorPago());
			
			for(ItemVenda item : getEntity().getItens()) {
				for(Adicional adicional : service.carregaAdicionais(item)) {
					adicional.setQuantidade(new Double(1));
					item.getAdicionais().add(adicional);
				}
			}
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao localizar venda. " + e.getMessage());
		}
	}
	
	public void btnFecharConta() {
		try {	
			setEntity(service.salvar(getEntity(), getPagamento()));
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao fechar conta. " + e.getMessage());
		}
	}
	
	public void btnIncluirPagamento() {
		try {			
			PagamentoVenda pagamento = new PagamentoVenda();
			pagamento = service.incluiPagamento(getPagamento(), getEntity().getPagamentos(), valorFaltante);
			
			pagamento.setVenda(getEntity());
			getEntity().getPagamentos().add(pagamento);
			
			getPagamento().setValorPago(service.calculaTotalPago(getEntity().getPagamentos()));
			getPagamento().setValorTroco(service.calculaTroco(getPagamento().getValorRecebido(), valorFaltante, pagamento.getFormaPagamento()));
			
			pagamento.setValorPago(getPagamento().getValorPago());
			pagamento.setValorTroco(getPagamento().getValorTroco());
			pagamento.setDesconto(getEntity().getDesconto() == null ? new Double(0) : getEntity().getDesconto());
			pagamento.setTaxaEntrega(getEntity().getTaxaEntrega() == null ? new Double(0) : getEntity().getTaxaEntrega());
			
			valorFaltante = service.calculaValorFantante(getPagamento().getValorTotal(), getPagamento().getValorPago());
			getPagamento().setValorRecebido(null);
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao incluir pagamento. " + e.getMessage());
		}
	}
	
	public void btnSelecionaFormaPagamento(String descricao) {
		try {
			if(descricao != null) {			
				this.acao = descricao;
				FormaPagamento formaPagamento = service.buscaFormaPagamentoPorDescricao(descricao);
				
				pagamento.setBandeira(null);
				pagamento.setFormaPagamento(formaPagamento);
				this.bandeiras = service.buscaBandeiraPorFormaPagamentoVinculada(formaPagamento);
			}
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}
	
	public void btnEstornar() {
		this.pagamento.setValorPago(this.pagamento.getValorPago() - getPagamentoSelecionado().getValorRecebido());
		valorFaltante = service.calculaValorFantante(getPagamento().getValorTotal(), getPagamento().getValorPago());
		getPagamento().setValorTroco(service.estornarFormaPagamento(getPagamento(), getPagamentoSelecionado().getFormaPagamento(), valorFaltante));	
		
		getEntity().getPagamentos().remove(getPagamentoSelecionado());
	}
	
	public void btnLimpar() {
		getPagamento().setValorRecebido(new Double(0));
	}
	
	public void substituiValor(double valor) {
		this.pagamento.setValorRecebido(valor);
	}
	
	public void aplicaDesconto() {
		Double desconto = getEntity().getDesconto() != null ? getEntity().getDesconto() : new Double(0);
		Double acrescimo = getEntity().getTaxaEntrega() != null ? getEntity().getTaxaEntrega() : new Double(0);
		pagamento.setValorTotal(service.calculaFaltaPagar(new AplicaDescontoService(), getEntity().getSubtotal(), desconto, acrescimo));
		valorFaltante = service.calculaValorFantante(pagamento.getValorTotal(), pagamento.getValorPago());
	}
	
	public void aplicaTaxaEntrega() {
		Double acrescimo = getEntity().getTaxaEntrega() != null ? getEntity().getTaxaEntrega() : new Double(0);
		Double desconto = getEntity().getDesconto() != null ? getEntity().getDesconto() : new Double(0);
		pagamento.setValorTotal(service.calculaFaltaPagar(new AplicaTaxaEntregaService(), getEntity().getSubtotal(), desconto, acrescimo));
		valorFaltante = service.calculaValorFantante(pagamento.getValorTotal(), pagamento.getValorPago());
	}
	
	public Double calculaSubTotalItem(ItemVenda item) {
		return service.calculaSubTotalItem(item);
	}
	
	public String converter(Double valor) {
		return StringUtil.converterDouble(valor);
	}

	public void btnVoltar() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("venda-mesa-comanda.xhtml?view=search&faces-redirect=true&codigo=" + codigo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void beforeSearch() {
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	
	public String getAcao() {
		return acao;
	}

	public List<Bandeira> getBandeiras() {
		return bandeiras;
	}

	public void setBandeiras(List<Bandeira> bandeiras) {
		this.bandeiras = bandeiras;
	}

	public PagamentoVenda getPagamento() {
		return pagamento;
	}

	public void setPagamento(PagamentoVenda pagamento) {
		this.pagamento = pagamento;
	}
	
	public Double getValorFaltante() {
		return valorFaltante;
	}

	public Double getTotalPagar() {
		return totalPagar;
	}

	public void setTotalPagar(Double totalPagar) {
		this.totalPagar = totalPagar;
	}
	
	public PagamentoVenda getPagamentoSelecionado() {
		return pagamentoSelecionado;
	}

	public void setPagamentoSelecionado(PagamentoVenda pagamentoSelecionado) {
		this.pagamentoSelecionado = pagamentoSelecionado;
	}

	public boolean isPago() {
		if(this.valorFaltante.equals(BigDecimal.ZERO.doubleValue())) {
			return false;
		}
		return true;
	}
	
	public boolean isNotPago() {
		return !isPago();
	}
}
