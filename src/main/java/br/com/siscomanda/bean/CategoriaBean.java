package br.com.siscomanda.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.siscomanda.base.bean.BaseBean;
import br.com.siscomanda.exception.NapuleException;
import br.com.siscomanda.model.Categoria;
import br.com.siscomanda.service.CategoriaService;
import br.com.siscomanda.util.JSFUtil;

@Named
@ViewScoped
public class CategoriaBean extends BaseBean<Categoria> implements Serializable {

	private static final long serialVersionUID = -3066996558123931260L;
	
	@Inject
	private CategoriaService categoriaService;
	
	private List<Categoria> categorias;
	
	private Categoria categoriaSelecionado;
	
	private List<Categoria> categoriasSelecionados;
	
	@Override 
	public void init() {
	}
	
	public void btnRemover() {
		try {
			categoriaService.remover(getCategoriasSelecionados());
			getElements().removeAll(getCategoriasSelecionados());
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro(s) removido(s) com sucesso.");
		} catch (NapuleException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao remover. " + e.getMessage());
		}
	}
	
	public void btnSalvar() {
		try {			
			categoriaService.salvar(getEntity());
			setEntity(new Categoria());
			getEstadoViewBean().setUpdate(false);
		}
		catch(NapuleException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar. " + e.getMessage());
		}
	}
	
	public void btnPesquisar() {
		try {	
			setElements(categoriaService.pesquisar(getEntity()));
		}
		catch(NapuleException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_WARN, e.getMessage());
		}
	}
	
	@Override
	protected void beforeSearch() {
		if(getEstadoViewBean().getSearch()) {
			setElements(categoriaService.todos());
		}
		setEntity(new Categoria());
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

	public Categoria getCategoriaSelecionado() {
		return categoriaSelecionado;
	}

	public void setCategoriaSelecionado(Categoria categoriaSelecionado) {
		this.categoriaSelecionado = categoriaSelecionado;
	}

	public List<Categoria> getCategoriasSelecionados() {
		return categoriasSelecionados;
	}

	public void setCategoriasSelecionados(List<Categoria> categoriasSelecionados) {
		this.categoriasSelecionados = categoriasSelecionados;
	}
}
