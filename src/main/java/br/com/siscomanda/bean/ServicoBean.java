package br.com.siscomanda.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.siscomanda.base.bean.BaseBean;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Servico;
import br.com.siscomanda.service.ServicoService;
import br.com.siscomanda.util.JSFUtil;

@Named
@ViewScoped
public class ServicoBean extends BaseBean<Servico> implements Serializable {

	private static final long serialVersionUID = -3066996558123931260L;
	
	@Inject
	private ServicoService servicoService;
	
	private List<Servico> servicos;
	
	private Servico servicoSelecionado;
	
	private List<Servico> servicosSelecionados;
	
	@Override 
	public void init() {
	}
	
	public void btnRemover() {
		try {
			servicoService.remover(getServicosSelecionados());
			getElements().removeAll(getServicosSelecionados());
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro(s) removido(s) com sucesso.");
		} catch (SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao remover. " + e.getMessage());
		}
	}
	
	public void btnSalvar() {
		try {			
			servicoService.salvar(getEntity());
			setEntity(new Servico());
			getEstadoViewBean().setUpdate(false);
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar. " + e.getMessage());
		}
	}
	
	public void btnPesquisar() {
		try {	
			setElements(servicoService.pesquisar(getEntity()));
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_WARN, e.getMessage());
		}
	}
	
	@Override
	protected void beforeSearch() {
		if(getEstadoViewBean().getSearch()) {
			setElements(servicoService.todos());
		}
		setEntity(new Servico());
	}

	public ServicoService getServicoService() {
		return servicoService;
	}

	public void setServicoService(ServicoService servicoService) {
		this.servicoService = servicoService;
	}

	public Servico getServicoSelecionado() {
		return servicoSelecionado;
	}

	public void setServicoSelecionado(Servico servicoSelecionado) {
		this.servicoSelecionado = servicoSelecionado;
	}

	public List<Servico> getServicosSelecionados() {
		return servicosSelecionados;
	}

	public void setServicosSelecionados(List<Servico> servicosSelecionados) {
		this.servicosSelecionados = servicosSelecionados;
	}

	public List<Servico> getServicos() {
		return servicos;
	}
}
