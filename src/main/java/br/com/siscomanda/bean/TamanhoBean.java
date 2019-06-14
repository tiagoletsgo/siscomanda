package br.com.siscomanda.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.siscomanda.base.bean.BaseBean;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.SubCategoria;
import br.com.siscomanda.model.Tamanho;
import br.com.siscomanda.service.SubCategoriaService;
import br.com.siscomanda.service.TamanhoService;
import br.com.siscomanda.util.JSFUtil;

@Named
@ViewScoped
public class TamanhoBean extends BaseBean<Tamanho> implements Serializable {

	private static final long serialVersionUID = 6807737026561133860L;

	@Inject
	private TamanhoService tamanhoService;
		
	@Inject
	private SubCategoriaService subCategoriaService;
	
	private List<Tamanho> tamanhos;
	
	private Tamanho tamanhoSelecionado;
	
	private List<Tamanho> tamanhosSelecionados;
	
	private List<SubCategoria> subCategorias;
	
	@Override 
	public void init() {
		subCategorias = subCategoriaService.todos();
	}
	
	public void btnRemover() {
		try {
			tamanhoService.remover(getTamanhosSelecionados());
			getElements().removeAll(getTamanhosSelecionados());
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro(s) removido(s) com sucesso.");
		} catch (SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao remover. " + e.getMessage());
		}
	}
	
	public void btnSalvar() {
		try {			
			tamanhoService.salvar(getEntity());
			setEntity(new Tamanho());
			getEstadoViewBean().setUpdate(false);
			beforeSearch();
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar. " + e.getMessage());
		}
	}
	
	public void btnPesquisar() {
		try {	
			setElements(tamanhoService.pesquisar(getEntity()));
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_WARN, e.getMessage());
		}
	}
	
	@Override
	protected void beforeSearch() {
		if(getEstadoViewBean().getSearch()) {
			setElements(tamanhoService.todos());
		}
		setEntity(new Tamanho());
	}
	
	@Override
	public void btnEditar(Tamanho tamanho) {
		try {	
			tamanho = tamanhoService.porTamanho(tamanho);
			super.btnEditar(tamanho);
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}

	public List<Tamanho> getTamanhos() {
		return tamanhos;
	}

	public void setTamanhos(List<Tamanho> tamanhos) {
		this.tamanhos = tamanhos;
	}

	public Tamanho getTamanhoSelecionado() {
		return tamanhoSelecionado;
	}

	public void setTamanhoSelecionado(Tamanho tamanhoSelecionado) {
		this.tamanhoSelecionado = tamanhoSelecionado;
	}

	public List<Tamanho> getTamanhosSelecionados() {
		return tamanhosSelecionados;
	}

	public void setTamanhosSelecionados(List<Tamanho> tamanhosSelecionados) {
		this.tamanhosSelecionados = tamanhosSelecionados;
	}
	
	public List<SubCategoria> getSubCategorias() {
		return subCategorias;
	}
}
