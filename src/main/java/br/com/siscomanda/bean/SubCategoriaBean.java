package br.com.siscomanda.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.siscomanda.base.bean.BaseBean;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Categoria;
import br.com.siscomanda.model.SubCategoria;
import br.com.siscomanda.service.CategoriaService;
import br.com.siscomanda.service.SubCategoriaService;
import br.com.siscomanda.util.JSFUtil;

@Named
@ViewScoped
public class SubCategoriaBean extends BaseBean<SubCategoria> implements Serializable {

	private static final long serialVersionUID = -3066996558123931260L;
	
	@Inject
	private SubCategoriaService subCategoriaService;
	
	@Inject
	private CategoriaService categoriaService;
	
	private List<SubCategoria> subCategorias;
	
	private SubCategoria subCategoriaSelecionado;
	
	private List<SubCategoria> subCategoriasSelecionados;
	
	private List<Categoria> categorias;
	
	@Override 
	public void init() {
		this.categorias = categoriaService.todos();
	}
	
	public void btnRemover() {
		try {
			subCategoriaService.remover(getSubCategoriasSelecionados());
			getElements().removeAll(getSubCategoriasSelecionados());
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro(s) removido(s) com sucesso.");
		} catch (SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao remover. " + e.getMessage());
		}
	}
	
	public void btnSalvar() {
		try {			
			subCategoriaService.salvar(getEntity());
			setEntity(new SubCategoria());
			getEstadoViewBean().setUpdate(false);
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar. " + e.getMessage());
		}
	}
	
	public void btnPesquisar() {
		try {	
			setElements(subCategoriaService.pesquisar(getEntity()));
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_WARN, e.getMessage());
		}
	}
	
	public void carregaCategoria() {
		if(this.categorias == null || this.categorias.isEmpty()) {			
			this.categorias = categoriaService.todos();
		}
	}
	
	@Override
	protected void beforeSearch() {
		if(getEstadoViewBean().getSearch()) {
			setElements(subCategoriaService.todos());
		}
		setEntity(new SubCategoria());
	}

	public SubCategoriaService getSubCategoriaService() {
		return subCategoriaService;
	}

	public void setSubCategoriaService(SubCategoriaService subCategoriaService) {
		this.subCategoriaService = subCategoriaService;
	}

	public SubCategoria getSubCategoriaSelecionado() {
		return subCategoriaSelecionado;
	}

	public void setSubCategoriaSelecionado(SubCategoria subCategoriaSelecionado) {
		this.subCategoriaSelecionado = subCategoriaSelecionado;
	}

	public List<SubCategoria> getSubCategoriasSelecionados() {
		return subCategoriasSelecionados;
	}

	public void setSubCategoriasSelecionados(List<SubCategoria> subCategoriasSelecionados) {
		this.subCategoriasSelecionados = subCategoriasSelecionados;
	}

	public List<SubCategoria> getSubCategorias() {
		return subCategorias;
	}
	
	public List<Categoria> getCategorias() {
		return categorias;
	}
}
