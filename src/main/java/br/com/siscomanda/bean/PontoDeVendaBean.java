package br.com.siscomanda.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
import br.com.siscomanda.exception.SiscomandaRuntimeException;
import br.com.siscomanda.model.Adicional;
import br.com.siscomanda.model.Cliente;
import br.com.siscomanda.model.ConfiguracaoGeral;
import br.com.siscomanda.model.ItemVenda;
import br.com.siscomanda.model.Preco;
import br.com.siscomanda.model.Produto;
import br.com.siscomanda.model.Tamanho;
import br.com.siscomanda.model.Venda;
import br.com.siscomanda.service.AdicionalService;
import br.com.siscomanda.service.ClienteService;
import br.com.siscomanda.service.ConfiguracaoGeralService;
import br.com.siscomanda.service.PontoDeVendaService;
import br.com.siscomanda.service.PrecoService;
import br.com.siscomanda.service.ProdutoService;
import br.com.siscomanda.service.UsuarioService;
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
	
	@Inject
	private ClienteService clienteService;
	
	@Inject
	private UsuarioService usuarioService;
	
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
	
	private ConfiguracaoGeral configuracao;
	
	private List<Cliente> clientes;
	
	private String pesquisaDescricaoProduto;
	
	private String pesquisaPorNomeCliente;
	
	private boolean modificandoItem;
	
	private Venda vendaSelecionada;
	
	private int index;
	
	@Override
	protected void init() {
		vendaBuilder = new VendaBuilder(getEntity());
		parametros = new HashMap<String, Object>();
		itensMeioAmeio = new ArrayList<ItemVenda>();
		produtosSelecionados = new ArrayList<Produto>();
		complementos = new ArrayList<Adicional>();
		configuracao = configuracaoService.definicaoSistema();
		clientes = clienteService.todos();
		item = new ItemVenda();
		
		setElements(pontoDeVendaService.vendasNaoPagas());
		initEntity();
	}
	
	private void initEntity() {
		try {
			String codigo = JSFUtil.getRequest().getParameter("codigo");
			vendaBuilder.comOperador(usuarioService.porCodigo(1L));
			setEntity(Objects.isNull(codigo) ? vendaBuilder.construir() : pontoDeVendaService.porCodigo(new Long(codigo)));
			
			if(Objects.nonNull(codigo)) {
				getEstadoViewBean().setCurrentView(EStateView.UPDATE);
			}
			
			codigo = null;
			
		} catch (NumberFormatException | SiscomandaException e) {
			e.printStackTrace();
		}
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
		this.complementos = new ArrayList<Adicional>();
		this.itensMeioAmeio = new ArrayList<ItemVenda>();
		getEstadoViewBean().setCurrentView(EStateView.INSERT);
	}
	
	public void btnSalvarVenda() {
		try {
			Venda venda = pontoDeVendaService.salvar(getEntity());
			venda = pontoDeVendaService.porCodigo(venda.getId());
			setEntity(venda);
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}
	
	public void btnCancelarVenda() {
		try {
			pontoDeVendaService.cancelar(getEntity());
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro cancelado com sucesso.");
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}
	
	public void btnRemoverVenda() {
		try {
			
			if(Objects.nonNull(getVendaSelecionada())) {
				setEntity(getVendaSelecionada());
			}
			
			setEntity(pontoDeVendaService.porCodigo(getEntity().getId()));
			pontoDeVendaService.remover(getEntity());
			setEntity(new Venda());
			setVendaSelecionada(null);
			
			init();
			getEstadoViewBean().setCurrentView(EStateView.SEARCH);
			
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro excluído com sucesso.");
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}
	
	public void btnIncluir(Produto produto) {
		try {
			setNovoItem(false);
			setIncluirItem(true);
			
			Produto prod = produto.clone(produto);
			setItem(pontoDeVendaService.paraItemVenda(prod));

			precos = precoService.porProduto(prod);
			produtos = produtoService.todos(true);
			produtos.remove(prod);
			
			parametros.put("descricaoProduto", prod.getDescricao());
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}
	
	public void btnCancelarItem() {
		boolean novoItem = false;
		boolean incluirItem = true;
		
		if(modificandoItem) {
			novoItem = false;
			incluirItem = false;
			getEstadoViewBean().setCurrentView(EStateView.UPDATE);
		}
		
		modificandoItem = false;
		setIncluirItem(novoItem);
		setNovoItem(incluirItem);
		getItensMeioAmeio().clear();		
		setItem(new ItemVenda());
		this.complementos = new ArrayList<>();
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
			getItem().setTamanho((Tamanho)parametros.get("tamanho"));
			ItemVenda item = getItem().clone(getItem());
			
			itens = pontoDeVendaService.personalizar(getProdutosSelecionados(), getItensMeioAmeio(), item);
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
	
	// remove um item quando esta sendo personalizado
	//
	public void btnRemover(int index, ItemVenda item) {
		try {
			item.setId(new Long(index));
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
		getItem().setTotal(total);
	}
	
	private void desmacarListaComplementos() {
		complementos = pontoDeVendaService.desmarcarListaDeComplementos(getComplementos(), getItensMeioAmeio(), getItem());
	}
	
	public String converteValorFracionado(Double valor) {
		return StringUtil.converterValorFracionado(valor);
	}
	
	public void btnConfirmar() {
		
		vendaBuilder = pontoDeVendaService.confirmarItem(vendaBuilder, getItensMeioAmeio(), getItem(), modificandoItem);
		modificandoItem = false;
		
		vendaBuilder
		.comNumeroVenda(getEntity().getId())
		.comControle(getEntity().getControle())
		.comDesconto(getEntity().getDesconto())
		.comTaxaEntrega(getEntity().getTaxaEntrega())
		.comValorPago(getEntity().getValorPago())
		.comTaxaServico(configuracao.getTaxaServico())
		.comTipoVenda(getEntity().getTipoVenda())
		.comStatus(getEntity().getStatus())
		.comDataHora(getEntity().getDataHora())
		.comOperador(usuarioService.porCodigo(1L));
		
		setEntity(vendaBuilder.construir());
		getEntity().calculaValorTotalDaVenda();
		
		setItem(new ItemVenda());
		setIncluirItem(false);
		btnVoltar();
	}
	
	public void btnSalvarTipoVenda() {
		try {
			setEntity(pontoDeVendaService.salvarTipoVenda(getEntity()));
			getEntity().calculaValorTotalDaVenda();
			
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Tipo venda selecionado com sucesso.");
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
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
	
	public void pesquisaNomeCliente() {
		try {
			Cliente cliente = new Cliente();
			cliente.setNomeCompleto(pesquisaPorNomeCliente);
			clientes = clienteService.pesquisar(cliente);
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}
	
	public void btnEditarVenda(Venda venda) {
		try {
			
			venda = pontoDeVendaService.porCodigo(venda.getId());
			
			getEstadoViewBean().setCurrentView(EStateView.UPDATE);
			
			vendaBuilder
			.comNumeroVenda(venda.getId())
			.comControle(venda.getControle())
			.comDesconto(venda.getDesconto())
			.comTaxaEntrega(venda.getTaxaEntrega())
			.comValorPago(venda.getValorPago())
			.comTaxaServico(configuracao.getTaxaServico())
			.comItens(venda.getItens())
			.comOperador(venda.getOperador())
			.comTipoVenda(venda.getTipoVenda())
			.comStatus(venda.getStatus())
			.comDataHora(venda.getDataHora());
			
			setEntity(vendaBuilder.construir());
			getEntity().calculaValorTotalDaVenda();
			
		} catch (SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}
	
	public void btnEditarItem(int index, ItemVenda item) {
		try {
			
			pontoDeVendaService.validaSituacaoVenda(getEntity());
			modificandoItem = true;
			setItem(getEntity().getItem(index));

			parametros.put("descricaoProduto", getItem().getProduto().getDescricao());
			
			precos = precoService.porProduto(getItem().getProduto());
			this.complementos = pontoDeVendaService.carregarItensComplementares(adicionalService.todos(), getItem());
			
			setNovoItem(false);
			setIncluirItem(true);
			getEstadoViewBean().setCurrentView(null);
		}
		catch (SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}
	
	public void btnRemoveItem(int index, ItemVenda item) {
		try {
			pontoDeVendaService.validaSituacaoVenda(getEntity());
			getEntity().removerItem(index, item);
			setItemSelecionado(null);
			
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro removido com sucesso. Para confirmar exclusão salve a venda.");
		}
		catch(SiscomandaRuntimeException | SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}

	@Override
	protected void beforeSearch() {
		parametros.put("venda", getEntity()); 
		setElements(pontoDeVendaService.buscarPor(parametros));
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
	
	public List<Integer> getControladores() {
		return pontoDeVendaService.gerarControladores(configuracao);
	}
	
	public List<Cliente> getClientes() {
		return clientes;
	}

	public String getPesquisaPorNomeCliente() {
		return pesquisaPorNomeCliente;
	}

	public void setPesquisaPorNomeCliente(String pesquisaPorNomeCliente) {
		this.pesquisaPorNomeCliente = pesquisaPorNomeCliente;
	}
	
	public EStatus[] getStatusVenda() {
		return EStatus.values();
	}
	
	public boolean isModificandoItem() {
		return modificandoItem;
	}

	public Venda getVendaSelecionada() {
		return vendaSelecionada;
	}

	public void setVendaSelecionada(Venda vendaSelecionada) {
		this.vendaSelecionada = vendaSelecionada;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
