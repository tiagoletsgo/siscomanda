package br.com.siscomanda.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
	
	private Produto produtoSelecionado;
	
	private Integer quantidade;
	
	private List<Integer> mesasComandas;
	
	private ETamanho tamanho;
	
	private List<Produto> selectManyCheckBoxProdutos;
	
	private List<Adicional> selectManyCheckBoxAdicionais;
	
	private List<Produto> produtos;
	
	private List<Adicional> adicionais;
	
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
		setQuantidade(new BigDecimal(1).intValue());
		
		mesasComandas = service.geraMesasComandas();
		adicionais = service.getAdicionais();
		produtos = service.buscaProduto("PIZZA");
	}
	
	public static void main(String[] args) {
		double valor = 1.99;
		DecimalFormat format = new DecimalFormat("#.##");
		System.out.println("R$" + format.format(valor));
	}
		
	public void btnAdicionaItem(Produto produto) {
		ItemVenda item = service.adicionaItemPedidoVenda(produto);
		item.setId(service.setIdTemporarioItem(getEntity().getItens()));
		service.adicionaItem(getEntity().getItens(), item);
		service.ordenarItemMenorParaMaior(getEntity().getItens());
		afterAction();
	}
	
	public void btnAdicionar() {
		
	}
	
	public void ajaxPesquisar() {
		Produto produto = service.buscaProduto(produtoSelecionado);
		produtos = service.buscaProduto(filterPesquisar, produto.getSubCategoria());
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
		selectManyCheckBoxProdutos = service.validaQuantidadePermitida(selectManyCheckBoxProdutos);
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
	
	public String formatMoeda(Double valor) {
		return "R$ " + StringUtil.parseDouble(valor);
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

	public void setTamanho(ETamanho tamanho) {
		this.tamanho = tamanho;
	}

	public List<Produto> getSelectManyCheckBoxProdutos() {
		return selectManyCheckBoxProdutos;
	}

	public void setSelectManyCheckBoxProdutos(List<Produto> selectManyCheckBoxProdutos) {
		this.selectManyCheckBoxProdutos = selectManyCheckBoxProdutos;
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

	public ETamanho getTamanho() {
		return tamanho;
	}

	public String getFilterPesquisar() {
		return filterPesquisar;
	}

	public void setFilterPesquisar(String filterPesquisar) {
		this.filterPesquisar = filterPesquisar;
	}
}
