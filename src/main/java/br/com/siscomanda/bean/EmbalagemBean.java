package br.com.siscomanda.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.siscomanda.base.bean.BaseBean;
import br.com.siscomanda.exception.NapuleException;
import br.com.siscomanda.model.Embalagem;
import br.com.siscomanda.service.EmbalagemService;
import br.com.siscomanda.util.JSFUtil;

@Named
@ViewScoped
public class EmbalagemBean extends BaseBean<Embalagem> implements Serializable {

	private static final long serialVersionUID = -7071829711995344800L;

	@Inject
	private EmbalagemService embalagemService;
	
	private List<Embalagem> embalagens;
	
	private Embalagem embalagemSelecionado;
	
	private List<Embalagem> embalagensSelecionados;
	
	@Override 
	public void init() {
	}
	
	public void btnRemover() {
		try {
			embalagemService.remover(getEmbalagensSelecionados());
			getElements().removeAll(getEmbalagensSelecionados());
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro(s) removido(s) com sucesso.");
		} catch (NapuleException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao remover. " + e.getMessage());
		}
	}
	
	public void btnSalvar() {
		try {			
			embalagemService.salvar(getEntity());
			setEntity(new Embalagem());
			getEstadoViewBean().setUpdate(false);
		}
		catch(NapuleException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar. " + e.getMessage());
		}
	}
	
	public void btnPesquisar() {
		try {	
			setElements(embalagemService.pesquisar(getEntity()));
		}
		catch(NapuleException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_WARN, e.getMessage());
		}
	}
	
	@Override
	protected void beforeSearch() {
		if(getEstadoViewBean().getSearch()) {
			setElements(embalagemService.todos());
		}
		setEntity(new Embalagem());
	}

	public EmbalagemService getEmbalagemService() {
		return embalagemService;
	}

	public void setEmbalagemService(EmbalagemService embalagemService) {
		this.embalagemService = embalagemService;
	}

	public Embalagem getEmbalagemSelecionado() {
		return embalagemSelecionado;
	}

	public void setEmbalagemSelecionado(Embalagem embalagemSelecionado) {
		this.embalagemSelecionado = embalagemSelecionado;
	}

	public List<Embalagem> getEmbalagensSelecionados() {
		return embalagensSelecionados;
	}

	public void setEmbalagensSelecionados(List<Embalagem> embalagensSelecionados) {
		this.embalagensSelecionados = embalagensSelecionados;
	}

	public List<Embalagem> getEmbalagens() {
		return embalagens;
	}
}
