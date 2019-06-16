package br.com.siscomanda.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.siscomanda.base.bean.BaseBean;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Preco;
import br.com.siscomanda.model.Produto;
import br.com.siscomanda.model.SubCategoria;
import br.com.siscomanda.model.Tamanho;
import br.com.siscomanda.service.ProdutoService;
import br.com.siscomanda.service.SubCategoriaService;
import br.com.siscomanda.service.TamanhoService;
import br.com.siscomanda.util.JSFUtil;

@Named
@ViewScoped
public class ProdutoBean extends BaseBean<Produto> implements Serializable {

	private static final long serialVersionUID = -1485316002386206768L;

	@Inject
	private ProdutoService produtoService;
	
	@Inject
	private TamanhoService tamanhoService;
	
	@Inject
	private SubCategoriaService subCategoriaService;
	
	private List<Produto> produtos;
	
	private Produto produtoSelecionado;
	
	private List<Produto> produtosSelecionados;
	
	private List<SubCategoria> subCategorias;
	
	private List<Tamanho> tamanhos;
	
	@Override 
	public void init() {
	}
	
	public void btnRemover() {
		try {
			produtoService.remover(getProdutosSelecionados());
			getElements().removeAll(getProdutosSelecionados());
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro(s) removido(s) com sucesso.");
		} catch (SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao remover. " + e.getMessage());
		}
	}
	
	public void carregaSubCategorias() {
		try {
			getEntity().getPrecos().clear();
			subCategorias = subCategoriaService.pesquisar(getEntity().getCategoria());
		} catch (SiscomandaException e) {
			subCategorias = null;
		}
	}
	
	public void carregaPreco() {
		getEntity().getPrecos().clear();
		this.tamanhos = tamanhoService.tamanhoPorSubCategoria(getEntity().getSubCategoria());
		for(Tamanho tamanho : tamanhos) {
			Preco preco = new Preco();
			preco.setTamanho(tamanho);
			preco.setProduto(getEntity());
			getEntity().getPrecos().add(preco);
		}
	}
	
	@Override
	public void btnNovo() {
		init();
		super.btnNovo();
	}
	
	public void btnSalvar() {
		try {			
			produtoService.salvar(getEntity());
			setEntity(new Produto());
			
			if(getEstadoViewBean().getUpdate()) {
				setElements(produtoService.todos());
				getEstadoViewBean().setUpdate(false);
			}
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar. " + e.getMessage());
		}
	}
	
	@Override
	public void btnEditar(Produto produto) {
		try {			
			produto = produtoService.porCodigo(produto);
			this.subCategorias = subCategoriaService.pesquisar(produto.getCategoria());
			super.btnEditar(produto);
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}
	
	public void btnPesquisar() {
		try {	
			setElements(produtoService.pesquisar(getEntity()));
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_WARN, e.getMessage());
		}
	}
	
	@Override
	protected void beforeSearch() {
		if(getEstadoViewBean().getSearch()) {
			setElements(produtoService.todos());
		}
		setEntity(new Produto());
	}

	public ProdutoService getProdutoService() {
		return produtoService;
	}

	public void setProdutoService(ProdutoService produtoService) {
		this.produtoService = produtoService;
	}

	public Produto getProdutoSelecionado() {
		return produtoSelecionado;
	}

	public void setProdutoSelecionado(Produto produtoSelecionado) {
		this.produtoSelecionado = produtoSelecionado;
	}

	public List<Produto> getProdutosSelecionados() {
		return produtosSelecionados;
	}

	public void setProdutosSelecionados(List<Produto> produtosSelecionados) {
		this.produtosSelecionados = produtosSelecionados;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}
	
	public List<SubCategoria> getSubCategorias() {
		return subCategorias;
	}
	
	public List<Tamanho> getTamanhos() {
		return tamanhos;
	}
}
