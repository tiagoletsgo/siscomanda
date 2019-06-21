package br.com.siscomanda.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.siscomanda.base.bean.BaseBean;
import br.com.siscomanda.builder.VendaBuilder;
import br.com.siscomanda.enumeration.EStateView;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Adicional;
import br.com.siscomanda.model.ItemVenda;
import br.com.siscomanda.model.Preco;
import br.com.siscomanda.model.Produto;
import br.com.siscomanda.model.Tamanho;
import br.com.siscomanda.model.Venda;
import br.com.siscomanda.service.AdicionalService;
import br.com.siscomanda.service.PontoDeVendaService;
import br.com.siscomanda.service.PrecoService;
import br.com.siscomanda.service.ProdutoService;
import br.com.siscomanda.util.JSFUtil;
import br.com.siscomanda.util.StringUtil;

@Named
@ViewScoped
public class PontoDeVendaBean extends BaseBean<Venda> implements Serializable {

	private static final long serialVersionUID = -2805014293863805310L;
	
	@Inject
	private PontoDeVendaService pontoDeVendaService;
	
	@Inject
	private PrecoService precoService;
	
	@Inject
	private ProdutoService produtoService;
	
	@Inject
	private AdicionalService adicionalService;
	
	private boolean novoItem;
	
	private boolean incluirItem;
	
	private VendaBuilder builder;
	
	private ItemVenda item;
	
	private ItemVenda itemSelecionado;

	private List<Preco> precos;
	
	private List<Adicional> complementos;
	
	private List<ItemVenda> itensMeioAmeio;
	
	private List<Produto> produtos;
	
	private List<Produto> produtosSelecionados;
	
	private Map<String, Object> parametros;
	
	@Override
	protected void init() {
		builder = new VendaBuilder();
		parametros = new HashMap<String, Object>();
		itensMeioAmeio = new ArrayList<ItemVenda>();
	}
	
	public void btnNovoItem() {
		setNovoItem(true);
		getEstadoViewBean().setCurrentView(null);
	}
	
	public void btnVoltar() {
		setNovoItem(false);
		getEstadoViewBean().setCurrentView(EStateView.INSERT);
	}
	
	public void btnIncluir(Produto produto) {
		try {
			setNovoItem(false);
			setIncluirItem(true);
			setItem(pontoDeVendaService.paraItemVenda(getEntity(), produto));

			getItem().setId(1L);
			precos = precoService.porProduto(produto);
			produtos = produtoService.todos();
			produtos.remove(produto);
			
			parametros.put("descricaoProduto", produto.getDescricao());
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}
	
	public void atualizaListaDeProdutos() {
		List<Produto> produtos = pontoDeVendaService.corrigeNomeDaListaDeProdutos(getProdutos());
		this.produtos = produtos;
	}
	
	public void alterarTamanho() {
		try {
			Preco preco = precoService.porValor(getItem().getValor(), getItem().getProduto());
			parametros.put("tamanho", preco.getTamanho());
			getItem().setTotal(getItem().getValor() * getItem().getQuantidade());
			complementos = adicionalService.todos();
			
			atualizaListaItensMeioAmeio();
			atualizaListaDeComplementos();
			atualizaNomeProduto();
			calcularValorTotal();
		}catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void atualizaNomeProduto() {
		String descricaoOriginalProduto = (String)parametros.get("descricaoProduto");
		Tamanho tamanho = (Tamanho)parametros.get("tamanho");
		String sigla = tamanho.getSigla();
		
		Map<String, Object> map = pontoDeVendaService.atualizaNomeDosProdutos(getItensMeioAmeio(), descricaoOriginalProduto, sigla);
		String nomeProduto = (String)map.get("descricaoProduto");
		List<ItemVenda> itens = (List<ItemVenda>)map.get("itens");
		itensMeioAmeio = itens;
		
		getItem().getProduto().setDescricao(nomeProduto);
	}
	
	private void atualizaListaItensMeioAmeio() {
		try {
			List<ItemVenda> itens = new ArrayList<ItemVenda>();
			Tamanho tamanho = (Tamanho)parametros.get("tamanho");
			itens = pontoDeVendaService.atualizaListaItemMeioAmeio(getItensMeioAmeio(), tamanho);
			itensMeioAmeio = itens;
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		finally {
			if(getProdutosSelecionados() != null) {
				getProdutosSelecionados().clear();
			}
		}
	}
	
	public void btnPersonalizar() {
		try {
			List<ItemVenda> itens = new ArrayList<ItemVenda>();
			Tamanho tamanho = (Tamanho)parametros.get("tamanho");
			ItemVenda item = getItem().clonar(getItem());
			itens = pontoDeVendaService.personalizar(getProdutosSelecionados(), getItensMeioAmeio(), tamanho, item, getEntity());
			itensMeioAmeio = itens;
			
			atualizaNomeProduto();
			calcularValorTotal();
		} catch (SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		finally {
			getProdutosSelecionados().clear();
		}
	}
	
	public void adicionaComplementos(Adicional adicional) {
		try {
			List<ItemVenda> itens = new ArrayList<ItemVenda>();
			
			itens = pontoDeVendaService.adicionaComplemento(getItemSelecionado(), getItem(), adicional, getComplementos(), getItensMeioAmeio());
			itensMeioAmeio = itens;
			
			atualizaListaDeComplementos();
			calcularValorTotal();
			
			listaMeioAmeioTemUmRegistro();
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}
	
	private void listaMeioAmeioTemUmRegistro() {
		List<ItemVenda> itens = pontoDeVendaService.listaItemMeioAmeioTiverUmRegistroRemovaTudo(getItensMeioAmeio());
		itensMeioAmeio = itens;
	}
	
	private void atualizaListaDeComplementos() {
		List<Adicional> complementos = new ArrayList<Adicional>();
		complementos = pontoDeVendaService.atualizaListaDeComplementos(getItemSelecionado(), getItem(), getComplementos());
		this.complementos = complementos;
	}
	
	public void btnRemover(ItemVenda item) {
		try {
			List<ItemVenda> itens = new ArrayList<ItemVenda>();
			itens = pontoDeVendaService.removerItem(getItensMeioAmeio(), item, getItem().getProduto());
			itensMeioAmeio = itens;
			
			atualizaListaItensMeioAmeio();
			atualizaListaDeComplementos();
			atualizaNomeProduto();
			calcularValorTotal();
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}
	
	public void calcularValorTotal() {
		double total = pontoDeVendaService.calcularValorTotal(getItensMeioAmeio(), getItem().getValor());
		getItem().setTotal(total * getItem().getQuantidade());
	}
		
	public String paraMoedaPtBR(Double valor) {
		return StringUtil.converterParaValorMonetario(valor);
	}

	@Override
	protected void beforeSearch() {
		
	}

	public boolean isNovoItem() {
		return novoItem;
	}

	public void setNovoItem(boolean novoItem) {
		this.novoItem = novoItem;
	}

	public boolean isIncluirItem() {
		return incluirItem;
	}

	public void setIncluirItem(boolean incluirItem) {
		this.incluirItem = incluirItem;
	}

	public ItemVenda getItem() {
		return item;
	}

	public void setItem(ItemVenda item) {
		this.item = item;
	}
	
	public List<Preco> getPrecos() {
		return precos;
	}
	
	public List<Adicional> getComplementos() {
		return complementos;
	}
	
	public List<ItemVenda> getItensMeioAmeio() {
		return itensMeioAmeio;
	}
	
	public List<Produto> getProdutos() {
		return produtos;
	}

	public List<Produto> getProdutosSelecionados() {
		return produtosSelecionados;
	}

	public void setProdutosSelecionados(List<Produto> produtosSelecionados) {
		this.produtosSelecionados = produtosSelecionados;
	}
	
	public ItemVenda getItemSelecionado() {
		return itemSelecionado;
	}
	
	public void setItemSelecionado(ItemVenda itemSelecionado) {
		this.itemSelecionado = itemSelecionado;
	}
}
