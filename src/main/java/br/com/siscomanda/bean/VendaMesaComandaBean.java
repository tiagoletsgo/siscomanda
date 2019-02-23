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
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Adicional;
import br.com.siscomanda.model.ItemVenda;
import br.com.siscomanda.model.Produto;
import br.com.siscomanda.model.Venda;
import br.com.siscomanda.service.VendaMesaComandaService;
import br.com.siscomanda.util.JSFUtil;
import br.com.siscomanda.util.StringUtil;

@Named
@ViewScoped
public class VendaMesaComandaBean extends BaseBean<Venda> implements Serializable {

	private static final long serialVersionUID = -7879816895142165754L;
	
	@Inject
	private VendaMesaComandaService service;
	
	private ItemVenda itemSelecionado;
	
	private Double quantidade;
	
	private List<Integer> mesasComandas;
	
	private List<Adicional> adicionais;
	
	private List<Adicional> selectManyCheckBoxAdicionais;

	private Produto produtoSelecionado;

	private List<Produto> produtos;
	
	private String filterPesquisar;
	
	@Override
	protected void init() {
		getEntity().setStatus(EStatus.EM_ABERTO);
		getEntity().setIniciado(new Date());
		getEntity().setSubtotal(new Double(0));
		getEntity().setTotal(new Double(0));
		getEntity().setTaxaServico(new Double(0));
		getEntity().setTaxaEntrega(new Double(0));
		getEntity().setDesconto(new Double(0));
		setQuantidade(new BigDecimal(1).doubleValue());
		
		mesasComandas = service.geraMesasComandas();
		produtos = service.buscaProduto("PIZZA");
		adicionais = service.getAdicionais();
	}
			
	public void btnAdicionaItem() {
		service.incluirItem(getEntity().getItens(), getSelectManyCheckBoxAdicionais(), getItemSelecionado(), getProdutoSelecionado(), getQuantidade());
		service.incluirAdicionais(getEntity().getItens(), getSelectManyCheckBoxAdicionais());
		afterAction();
	}
		
	public void btnRemoveItem(ItemVenda itemVenda, Produto produto) {
		try {
//			itemVenda = service.clonaItemVenda(itemVenda, produto, getQuantidade());
			service.removeItem(getEntity().getItens(), itemVenda, produto);
			afterAction();
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}

	public void ajaxPesquisar() {
		Produto produto = service.buscaProduto(produtoSelecionado);
		produtos = service.buscaProduto(filterPesquisar, produto.getSubCategoria());
	}
	
	public void ajaxPesquisaAdicional() {
		adicionais = service.buscaAdicionalPor(filterPesquisar);
	}
	
	public void actionListenerQuantidade(double valor) {
		this.quantidade = valor;
	}
	
	private void afterAction() {		
		getEntity().setSubtotal(service.calculaSubtotal(getEntity().getItens()));
		getEntity().setTaxaServico(getEntity().getSubtotal() * service.getTaxaServico());		
		getEntity().setTotal(service.calculaTotal(getEntity()));
		
		itemSelecionado = null;
		produtoSelecionado = null;
		selectManyCheckBoxAdicionais = null;
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
	
	public String formatMoeda(Double valor) {
		return StringUtil.parseDouble(valor);
	}
	
	public String converter(Double valor) {
		return StringUtil.converterDouble(valor);
	}
	
	public String getQuantidadeConverter() {
		return StringUtil.converterDouble(quantidade);
	}
	
	public void setQuantidadeConverter(String valor) {
		this.quantidade = Double.parseDouble(valor);
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

	public Double getQuantidade() {
		return quantidade;
	}
	
	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}

	public List<Adicional> getSelectManyCheckBoxAdicionais() {
		return selectManyCheckBoxAdicionais;
	}

	public void setSelectManyCheckBoxAdicionais(List<Adicional> selectManyCheckBoxAdicionais) {
		this.selectManyCheckBoxAdicionais = selectManyCheckBoxAdicionais;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}
	
	public List<Adicional> getAdicionais() {
		return adicionais;
	}

	public String getFilterPesquisar() {
		return filterPesquisar;
	}

	public void setFilterPesquisar(String filterPesquisar) {
		this.filterPesquisar = filterPesquisar;
	}
}
