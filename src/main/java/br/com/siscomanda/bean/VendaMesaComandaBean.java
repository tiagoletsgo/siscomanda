package br.com.siscomanda.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.siscomanda.base.bean.BaseBean;
import br.com.siscomanda.enumeration.EStatus;
import br.com.siscomanda.enumeration.ETamanho;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.ItemVenda;
import br.com.siscomanda.model.Produto;
import br.com.siscomanda.model.Venda;
import br.com.siscomanda.service.VendaMesaComandaService;
import br.com.siscomanda.util.JSFUtil;

@Named
@ViewScoped
public class VendaMesaComandaBean extends BaseBean<Venda> implements Serializable {

	private static final long serialVersionUID = -7879816895142165754L;
	
	@Inject
	private VendaMesaComandaService service;
	
	private ItemVenda itemSelecionado;
	
	private Produto produtoSelecionado;
	
	private Integer quantidade;
	
	private List<Integer> mesasComandas;
	
	private ETamanho tamanho;
	
	private List<Produto> itemsPersonalizados;
	
	@Override
	protected void init() {
		if(mesasComandas == null || mesasComandas.isEmpty()) {			
			mesasComandas = service.geraMesasComandas();
		}
		getEntity().setStatus(EStatus.EM_ABERTO);
		getEntity().setIniciado(new Date());
		getEntity().setSubtotal(new Double(0));
		getEntity().setTotal(new Double(0));
		getEntity().setTaxaServico(new Double(0));
		getEntity().setTaxaEntrega(new Double(0));
		getEntity().setDesconto(new Double(0));
		setQuantidade(new BigDecimal(1).intValue());
	}
		
	public void btnAdicionaItem(Produto produto) {
		ItemVenda item = service.adicionaItemPedidoVenda(produto);
		item.setId(service.setIdTemporarioItem(getEntity().getItens()));
		service.adicionaItem(getEntity().getItens(), item);
		service.ordenarItemMenorParaMaior(getEntity().getItens());
		afterAction();
	}
	
	public void btnRemoveItem(ItemVenda itemVenda, Produto produto) {
		try {
			itemVenda = service.clonaItemVenda(itemVenda, produto, getQuantidade());
			service.removeItem(getEntity().getItens(), itemVenda, produto);
			afterAction();
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}
	
	public void btnPersonalizar() {
		System.out.println("teste" + itemSelecionado.getProduto().getDescricao());
	}
	
	public ETamanho[] getTamanhos() {
		return ETamanho.values();
	}
	
	public void ajaxValidaQuantidadePermitida() {
		try {
			service.validaQuantidadePermitida(itemsPersonalizados);
		}
		catch(SiscomandaException e) {
			itemsPersonalizados.remove(itemsPersonalizados.get(1));
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}
	
	private void afterAction() {		
		getEntity().setSubtotal(service.calculaSubtotal(getEntity().getItens()));
		getEntity().setTaxaServico(getEntity().getSubtotal() * service.getTaxaServico());		
		getEntity().setTotal(service.calculaTotal(getEntity()));
	}
	
	@Override
	protected void beforeSearch() {
	}
	
	public List<Integer> getQuantidadeMesasComandas() {
		return mesasComandas;
	}

	public ItemVenda getItemSelecionado() {
		getEntity();
		if(itemSelecionado == null) {
			itemSelecionado = new ItemVenda();
		}
		return itemSelecionado;
	}

	public void setItemSelecionado(ItemVenda itemSelecionado) {
		this.itemSelecionado = itemSelecionado;
	}

	public Produto getProdutoSelecionado() {
		return produtoSelecionado;
	}

	public void setProdutoSelecionado(Produto produtoSelecionado) {
		this.produtoSelecionado = produtoSelecionado;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public List<Produto> getItemsPersonalizados() {
		return itemsPersonalizados;
	}

	public void setItemsPersonalizados(List<Produto> itemsPersonalizados) {
		this.itemsPersonalizados = itemsPersonalizados;
	}

	public void setTamanho(ETamanho tamanho) {
		this.tamanho = tamanho;
	}

	public ETamanho getTamanho() {
		return tamanho;
	}
}
