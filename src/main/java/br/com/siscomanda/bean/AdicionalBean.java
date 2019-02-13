package br.com.siscomanda.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.siscomanda.base.bean.BaseBean;
import br.com.siscomanda.enumeration.EFatorMedida;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Adicional;
import br.com.siscomanda.service.AdicionalService;
import br.com.siscomanda.util.JSFUtil;

@Named
@ViewScoped
public class AdicionalBean extends BaseBean<Adicional> implements Serializable {

	private static final long serialVersionUID = 8194647047375428193L;
	
	@Inject
	private AdicionalService service;
	
	private List<Adicional> adicionaisSelecionados;
	
	@Override
	protected void init() {
		getEntity().setPrecoVenda(new Double("0"));
		getEntity().setPrecoCusto(new Double("0"));
	}
	
	public void  btnSalvar() {
		try {			
			service.salvar(getEntity());
			setEntity(new Adicional());
			if(getEstadoViewBean().getUpdate()) {
				setElements(service.todos());
				getEstadoViewBean().setUpdate(false);
			}			
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}
	
	public void btnPesquisar() {
		try {	
			setElements(service.pesquisar(getEntity()));
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_WARN, e.getMessage());
		}
	}
	
	public void btnRemover() {
		try {
			service.remover(getAdicionaisSelecionados());
			getElements().removeAll(getAdicionaisSelecionados());
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro(s) removido(s) com sucesso.");
		} catch (SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao remover. " + e.getMessage());
		}
	}

	@Override
	protected void beforeSearch() {
		if(getEstadoViewBean().getSearch()) {
			setElements(service.todos());
		}
		setEntity(new Adicional());
	}
	
	public EFatorMedida[] getFatorMedida() {
		return EFatorMedida.values();
	}

	public List<Adicional> getAdicionaisSelecionados() {
		return adicionaisSelecionados;
	}

	public void setAdicionaisSelecionados(List<Adicional> adicionaisSelecionados) {
		this.adicionaisSelecionados = adicionaisSelecionados;
	}	
}
