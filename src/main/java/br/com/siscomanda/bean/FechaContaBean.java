package br.com.siscomanda.bean;

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
	
	private Venda venda;
	
	private Double valorFaltante;
	
	private Double totalPagar;
	
	private String acao;
	
	@Override
	protected void init() {
		if(!FacesContext.getCurrentInstance().isPostback()) {	
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext external = context.getExternalContext();
			
			codigo = Long.parseLong(external.getRequestParameterMap().get("codigo"));
			venda = new Venda(codigo);
			
			pagamento = new PagamentoVenda();
			pagamento.setValorPago(new Double(0));
			pagamento.setValorTroco(new Double(0));
			buscaVenda();
		}
	}
	
	public void btnSalvar() {
		
	}
	
	public void btnIncluirPagamento() {
		try {			
			PagamentoVenda pagamento = new PagamentoVenda();
			pagamento = service.incluiPagamento(getPagamento(), venda.getPagamentos(), valorFaltante);
			venda.getPagamentos().add(pagamento);
			
			getPagamento().setValorPago(service.calculaTotalPago(venda.getPagamentos()));
			getPagamento().setValorTroco(service.calculaTroco(getPagamento().getValorRecebido(), valorFaltante, pagamento.getFormaPagamento(), false));

			valorFaltante = service.calculaValorFantante(getPagamento().getValorTotal(), getPagamento().getValorPago());
			getPagamento().setValorRecebido(null);
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao incluir registro. " + e.getMessage());
		}
	}
	
	private void buscaVenda() {
		try {			
			venda = service.buscaVenda(venda);
			setEntity(venda);
			pagamento.setValorTotal(getEntity().getTotal());
			valorFaltante = pagamento.getValorTotal();
			
			for(ItemVenda item : getEntity().getItens()) {
				for(Adicional adicional : service.carregaAdicionais(item)) {
					adicional.setQuantidade(new Double(1));
					item.getAdicionais().add(adicional);
				}
			}
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao localizar venda" + e.getMessage());
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
//		getPagamento().setValorTroco(service.calculaTroco(getPagamentoSelecionado().getValorRecebido(), venda.getTotal(), getPagamentoSelecionado().getFormaPagamento()));
		
		Double troco = getPagamento().getValorPago() != 0.0 ? getPagamento().getValorTroco() : 0.0;
		if(getPagamentoSelecionado().getFormaPagamento().getDescricao().equals("DINHEIRO")) {
			if(getPagamento().getValorTotal() > getPagamento().getValorPago()) {
				troco = getPagamento().getValorTotal() - valorFaltante;			
			}
		}
		
		if(valorFaltante > 0) {
			troco = 0.0;
		}
		
		getPagamento().setValorTroco(troco);
		
		venda.getPagamentos().remove(getPagamentoSelecionado());
	}
	
	public void btnLimpar() {
		getPagamento().setValorRecebido(new Double(0));
	}
	
	public void substituiValor(double valor) {
		this.pagamento.setValorRecebido(valor);
	}
	
	public void aplicaDesconto() {
		Double desconto = getEntity().getDesconto() != null ? getEntity().getDesconto() : new Double(0);
		Double total = getEntity().getTotal();
		Double somatorio = (total - desconto) + (getEntity().getTaxaEntrega() != null ? getEntity().getTaxaEntrega() : new Double(0));
		pagamento.setValorTotal(somatorio);
		valorFaltante = pagamento.getValorTotal();
	}
	
	public void aplicaTaxaEntrega() {
		Double taxaEntrega = getEntity().getTaxaEntrega() != null ? getEntity().getTaxaEntrega() : new Double(0);
		Double total = getEntity().getTotal();
		Double somatorio = (total + taxaEntrega) - (getEntity().getDesconto() != null ? getEntity().getDesconto() : new Double(0));
		pagamento.setValorTotal(somatorio);
		valorFaltante = pagamento.getValorTotal();
	}
	
	public Double calculaSubTotalItem(ItemVenda item) {
		return service.calculaSubTotalItem(item);
	}
	
	public String converter(Double valor) {
		return StringUtil.converterDouble(valor);
	}
	
	public String btnVoltarCancelar() {
		return "/pages/modulo/venda/mesa/venda-mesa-comanda.xhtml?view=search&faces-redirect=true&codigo=" + codigo;
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
