package br.com.siscomanda.bean;

import java.io.Serializable;
import java.util.ArrayList;
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
	
	private Adicional adicionalSelecionado;

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
		getEntity().setValorPago(new Double(0));
		setQuantidade(new Double(1));
		
		mesasComandas = service.geraMesasComandas();
		produtos = service.buscaProduto("PIZZA");
		adicionais = service.getAdicionais();
	}
	
	public void btnSalvar() {		
		try {
			setEntity(service.salvar(getEntity()));
			getEntity().setItens(service.carregaItemVenda(getEntity()));
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar. " + e.getMessage());
		}
	}
	
	public void btnAdicionaItem() {
		try {
			service.incluirItem(getEntity(), getSelectManyCheckBoxAdicionais(), getItemSelecionado(), getProdutoSelecionado(), getQuantidade());
			afterAction();
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar. " + e.getMessage());
		}
	}
		
	public void btnRemoveItem() {
		try {
			ItemVenda item = service.clonar(produtoSelecionado, getSelectManyCheckBoxAdicionais());
			
			if(item == null) {
				item = service.clonar(itemSelecionado.getProduto(), itemSelecionado.getAdicionais());
				item.setId(itemSelecionado.getId());
				item.setQuantidade(itemSelecionado.getQuantidade());
			}
			
			List<ItemVenda> itens = service.removeItem(getEntity().getItens(), item, quantidade);
			getEntity().setItens(itens);
			afterAction();
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao remover. " + e.getMessage());
		}
	}
	
	public void btnRemoveAdicional() {
		itemSelecionado.getAdicionais().remove(adicionalSelecionado);
		afterAction();
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
		setQuantidade(new Double(1));
		
		filterPesquisar = null;
		itemSelecionado = null;
		produtoSelecionado = null;
		selectManyCheckBoxAdicionais = new ArrayList<>();
	}
	
	public Double calculaSubTotalItem(ItemVenda item) {
		return service.calculaSubTotalItem(item);
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

	public Adicional getAdicionalSelecionado() {
		return adicionalSelecionado;
	}

	public void setAdicionalSelecionado(Adicional adicionalSelecionado) {
		this.adicionalSelecionado = adicionalSelecionado;
	}
}
