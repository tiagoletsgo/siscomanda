package br.com.siscomanda.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.siscomanda.base.bean.BaseBean;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Tipo;
import br.com.siscomanda.service.TipoService;
import br.com.siscomanda.util.JSFUtil;

@Named
@ViewScoped
public class TipoBean extends BaseBean<Tipo> implements Serializable {

	private static final long serialVersionUID = 3751831122798001439L;

	@Inject
	private TipoService tipoService;
	
	private List<Tipo> tipos;
	
	private Tipo tipoSelecionado;
	
	private List<Tipo> tiposSelecionados;
	
	@Override 
	public void init() {
	}
	
	public void btnRemover() {
		try {
			tipoService.remover(getTiposSelecionados());
			getElements().removeAll(getTiposSelecionados());
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro(s) removido(s) com sucesso.");
		} catch (SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao remover. " + e.getMessage());
		}
	}
	
	public void btnSalvar() {
		try {			
			tipoService.salvar(getEntity());
			setEntity(new Tipo());
			getEstadoViewBean().setUpdate(false);
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar. " + e.getMessage());
		}
	}
	
	public void btnPesquisar() {
		try {	
			setElements(tipoService.pesquisar(getEntity()));
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_WARN, e.getMessage());
		}
	}
	
	@Override
	protected void beforeSearch() {
		if(getEstadoViewBean().getSearch()) {
			setElements(tipoService.todos());
		}
		setEntity(new Tipo());
	}

	public List<Tipo> getTipos() {
		return tipos;
	}

	public void setTipos(List<Tipo> tipos) {
		this.tipos = tipos;
	}

	public Tipo getTipoSelecionado() {
		return tipoSelecionado;
	}

	public void setTipoSelecionado(Tipo tipoSelecionado) {
		this.tipoSelecionado = tipoSelecionado;
	}

	public List<Tipo> getTiposSelecionados() {
		return tiposSelecionados;
	}

	public void setTiposSelecionados(List<Tipo> tiposSelecionados) {
		this.tiposSelecionados = tiposSelecionados;
	}	
}
