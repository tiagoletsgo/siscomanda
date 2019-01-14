package br.com.napule.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.napule.base.bean.BaseBean;
import br.com.napule.config.jsf.JSFUtil;
import br.com.napule.exception.NapuleException;
import br.com.napule.model.Bandeira;
import br.com.napule.service.BandeiraService;

@Named
@ViewScoped
public class BandeiraBean extends BaseBean<Bandeira> implements Serializable {

	private static final long serialVersionUID = -3066996558123931260L;
	
	@Inject
	private BandeiraService categoriaService;
	
	private List<Bandeira> bandeiras;
	
	private Bandeira bandeiraSelecionado;
	
	private List<Bandeira> bandeirasSelecionados;
	
	@Override 
	public void init() {
	}
	
	public void btnRemover() {
		try {
			categoriaService.remover(getBandeirasSelecionados());
			getElements().removeAll(getBandeirasSelecionados());
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro(s) removido(s) com sucesso.");
		} catch (NapuleException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao remover. " + e.getMessage());
		}
	}
	
	public void btnSalvar() {
		try {			
			categoriaService.salvar(getEntity());
			setEntity(new Bandeira());
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
		setEntity(new Bandeira());
	}

	public List<Bandeira> getBandeiras() {
		return bandeiras;
	}

	public void setBandeiras(List<Bandeira> bandeiras) {
		this.bandeiras = bandeiras;
	}

	public Bandeira getBandeiraSelecionado() {
		return bandeiraSelecionado;
	}

	public void setBandeiraSelecionado(Bandeira bandeiraSelecionado) {
		this.bandeiraSelecionado = bandeiraSelecionado;
	}

	public List<Bandeira> getBandeirasSelecionados() {
		return bandeirasSelecionados;
	}

	public void setBandeirasSelecionados(List<Bandeira> bandeirasSelecionados) {
		this.bandeirasSelecionados = bandeirasSelecionados;
	}
}
