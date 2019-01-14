package br.com.napule.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.napule.base.bean.BaseBean;
import br.com.napule.config.jsf.JSFUtil;
import br.com.napule.enumeration.EControlaEstoque;
import br.com.napule.exception.NapuleException;
import br.com.napule.model.Produto;
import br.com.napule.service.ProdutoService;

@Named
@ViewScoped
public class ProdutoBean extends BaseBean<Produto> implements Serializable {

	private static final long serialVersionUID = -1485316002386206768L;

	@Inject
	private ProdutoService produtoService;
	
	private List<Produto> produtos;
	
	private Produto produtoSelecionado;
	
	private List<Produto> produtosSelecionados;
	
	@Override 
	public void init() {
	}
	
	public void btnRemover() {
		try {
			produtoService.remover(getProdutosSelecionados());
			getElements().removeAll(getProdutosSelecionados());
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro(s) removido(s) com sucesso.");
		} catch (NapuleException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao remover. " + e.getMessage());
		}
	}
	
	public void btnSalvar() {
		try {			
			produtoService.salvar(getEntity());
			setEntity(new Produto());
			
			if(getEstadoViewBean().getUpdate()) {
				setElements(produtoService.todos());
			}
			
			getEstadoViewBean().setUpdate(false);
		}
		catch(NapuleException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar. " + e.getMessage());
		}
	}
	
	@Override
	public void btnEditar(Produto produto) {
		try {			
			produto = produtoService.porCodigo(produto);
			setEntity(produto);
			
			getEstadoViewBean().setCurrentView(false, true, false, true);
		}
		catch(NapuleException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}
	
	public void btnPesquisar() {
		try {	
			setElements(produtoService.pesquisar(getEntity()));
		}
		catch(NapuleException e) {
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
	
	public EControlaEstoque[] getControlaEstoque() {
		return EControlaEstoque.values();
	}
}
