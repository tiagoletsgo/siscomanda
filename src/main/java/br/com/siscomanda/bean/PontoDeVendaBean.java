package br.com.siscomanda.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import br.com.siscomanda.enumeration.EStatus;
import br.com.siscomanda.enumeration.ETipoVenda;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Adicional;
import br.com.siscomanda.model.ConfiguracaoGeral;
import br.com.siscomanda.model.ItemVenda;
import br.com.siscomanda.model.Preco;
import br.com.siscomanda.model.Produto;
import br.com.siscomanda.model.Tamanho;
import br.com.siscomanda.model.Venda;
import br.com.siscomanda.service.AdicionalService;
import br.com.siscomanda.service.ConfiguracaoGeralService;
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
	
	@Inject
	private ConfiguracaoGeralService configuracaoService;
	
	private boolean novoItem;
	
	private boolean incluirItem;
	
	private VendaBuilder vendaBuilder;
	
	private ItemVenda item;
	
	private ItemVenda itemSelecionado;

	private List<Preco> precos;
	
	private List<Adicional> complementos;
	
	private List<ItemVenda> itensMeioAmeio;
	
	private List<Produto> produtos;
	
	private List<Produto> produtosSelecionados;
	
	private Map<String, Object> parametros;
	
	private String pesquisaDescricaoProduto;
	
	private ConfiguracaoGeral configuracao;
	
	@Override
	protected void init() {
		vendaBuilder = new VendaBuilder();
		parametros = new HashMap<String, Object>();
		itensMeioAmeio = new ArrayList<ItemVenda>();
		produtosSelecionados = new ArrayList<Produto>();
		complementos = new ArrayList<Adicional>();
		
		vendaBuilder.comDataHora(new Date());
		vendaBuilder.comDesconto(new Double(0));
		vendaBuilder.comStatus(EStatus.EM_ABERTO);
		vendaBuilder.comSubtotal(new Double(0));
		vendaBuilder.comTaxaEntrega(new Double(0));
		vendaBuilder.comTaxaServico(new Double(0));
		vendaBuilder.comDesconto(new Double(0));
		setEntity(vendaBuilder.constroi());
		
		configuracao = configuracaoService.definicaoSistema();
	}
	
	public void btnNovoItem() {
		setNovoItem(true);
		getEstadoViewBean().setCurrentView(null);
		this.itensMeioAmeio = new ArrayList<ItemVenda>();
		this.complementos = new ArrayList<Adicional>();
		setItem(new ItemVenda());
		setItemSelecionado(null);
	}
	
	public void btnVoltar() {
		setNovoItem(false);
		getEstadoViewBean().setCurrentView(EStateView.INSERT);
	}
	
	public void btnIncluir(Produto produto) {
		try {
			setNovoItem(false);
			setIncluirItem(true);
			
			Produto prod = produto.clone(produto);
			setItem(pontoDeVendaService.paraItemVenda(getEntity(), prod));

			getItem().setId(1L);
			precos = precoService.porProduto(prod);
			produtos = produtoService.todos(true);
			produtos.remove(prod);
			
			parametros.put("descricaoProduto", prod.getDescricao());
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}
	
	public void btnCancelarInclusaoNovoItem() {
		setIncluirItem(false);
		setNovoItem(true);
		getItensMeioAmeio().clear();
		setItem(new ItemVenda());
		
		if(getComplementos() != null) {			
			getComplementos().clear();
		}
	}
	
	public void atualizaListaDeProdutos() {
		this.produtos = produtoService.todos(true);
		this.produtos.remove(getItem().getProduto());
		setPesquisaDescricaoProduto(StringUtil.addValorVazio());
		desmacarListaComplementos();
	}
	
	public void alterarTamanho() {
		try {
			Preco preco = precoService.porValor(getItem().getValor(), getItem().getProduto());
			parametros.put("tamanho", preco.getTamanho());
			getItem().setTamanho(preco.getTamanho());
			getItem().setTotal(getItem().getValor() * getItem().getQuantidade());
			
			if(getItem().getTamanho().isPermiteMeioAmeio()) {
				complementos = adicionalService.todos();
			}
			
			if(getItensMeioAmeio().isEmpty()) {
				getItensMeioAmeio().add(getItem());				
			}
			
			atualizaListaItensMeioAmeio();
			atualizaListaDeComplementos();
			atualizaNomeProduto();
			calcularValorTotal();
			
			listaMeioAmeioTemUmRegistro();
		}catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}
	
	private void atualizaNomeProduto() {
		int quantidadeItens = getItensMeioAmeio().size();
		String nomeProduto = quantidadeItens > 1 ? "PIZZA PERSONALIZADA" : (String)parametros.get("descricaoProduto");
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
			
			listaMeioAmeioTemUmRegistro();
			desmacarListaComplementos();
			
			getItem().setObservacao(StringUtil.addValorVazio());
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
		
		atualizaListaDeComplementos();
	}
	
	private void atualizaListaDeComplementos() {
		List<Adicional> complementos = new ArrayList<Adicional>();
		complementos = pontoDeVendaService.atualizaListaDeComplementos(getItemSelecionado(), getItem(), getComplementos());
		this.complementos = complementos;
		
		atualizaObservacao();
	}
	
	public void btnRemover(ItemVenda item) {
		try {
			setItemSelecionado(getItem());
			atualizaObservacao();
			
			setItemSelecionado(null);
			List<ItemVenda> itens = new ArrayList<ItemVenda>();
			itens = pontoDeVendaService.removerItem(getItensMeioAmeio(), item, getItem().getProduto());
			itensMeioAmeio = itens;
			
			atualizaListaItensMeioAmeio();
			atualizaListaDeComplementos();
			atualizaNomeProduto();
			calcularValorTotal();
			limparObservacaoEdesmacarListaDeComplemento();
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}
	
	public void incluirObservacao() {
		itensMeioAmeio = pontoDeVendaService.incluirObservacao(getItensMeioAmeio(), getItemSelecionado(), getItem().getObservacao());
	}
	
	public void atualizaObservacao() {
		getItem().setObservacao(pontoDeVendaService.observacao(getItensMeioAmeio(), getItemSelecionado() == null ? getItem() : getItemSelecionado()));
	}
	
	public void calcularValorTotal() {
		double total = pontoDeVendaService.calcularValorTotal(getItensMeioAmeio(), getItem());
		getItem().setTotal(total * getItem().getQuantidade());
	}
	
	private void desmacarListaComplementos() {
		complementos = pontoDeVendaService.desmarcarListaDeComplementos(getComplementos(), getItensMeioAmeio(), getItem());
	}
	
	public String converteValorFracionado(Double valor) {
		return StringUtil.converterValorFracionado(valor);
	}
	
	public void btnConfirmar() {
		
		if(getItensMeioAmeio().isEmpty()) {
			getItensMeioAmeio().add(getItem());
		}
		
		vendaBuilder.comItens(getItensMeioAmeio());
		vendaBuilder.comTaxaServico(configuracao.getTaxaServico());
		setEntity(vendaBuilder.constroi());
		setIncluirItem(false);
		btnVoltar();
	}
	
	public void limparObservacaoEdesmacarListaDeComplemento() {
		desmacarListaComplementos();
		if(getItensMeioAmeio().size() > 1) {			
			getItem().setObservacao(StringUtil.addValorVazio());
		}
	}
	
	public void buscaProdutoParaItemPersonalizado() {
		Produto produto = getItem().getProduto();
		this.produtos = pontoDeVendaService.localizaProdutoParaPersonalizar(getProdutos(), produto, pesquisaDescricaoProduto);
		if(!pesquisaDescricaoProduto.isEmpty()) {			
			parametros.put("produtosSelecionados", getProdutosSelecionados());
		}
	}
	
	@SuppressWarnings("unchecked")
	public void atualizaListaProdutoSelecionados() {
		List<Produto> listProdutoPrincipal = new ArrayList<Produto>();
		try {
			List<Produto> listProdutoSelecionado = (List<Produto>) parametros.get("produtosSelecionados");
			listProdutoPrincipal = pontoDeVendaService.atualizaListaProdutoSelecionado(getProdutosSelecionados(), listProdutoSelecionado);
			listProdutoPrincipal = pontoDeVendaService.validaQuantidadePermitida(listProdutoPrincipal, configuracao);
			parametros.remove("produtosSelecionados");			
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		finally {
			setProdutosSelecionados(listProdutoPrincipal);
		}
	}
	
	public String leftPad(String valor) {
		return StringUtil.leftPad(valor, 14, "0");
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

	public String getPesquisaDescricaoProduto() {
		return pesquisaDescricaoProduto;
	}

	public void setPesquisaDescricaoProduto(String pesquisaDescricaoProduto) {
		this.pesquisaDescricaoProduto = pesquisaDescricaoProduto;
	}
	
	public ConfiguracaoGeral getConfiguracao() {
		return configuracao;
	}
	
	public List<ETipoVenda> getTipoVendas() {
		return pontoDeVendaService.tiposDeVenda(configuracao);
	}
}
