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
import br.com.napule.model.FormaPagamento;
import br.com.napule.service.FormaPagamentoService;

@Named
@ViewScoped
public class FormaPagamentoBean extends BaseBean<FormaPagamento> implements Serializable {

	private static final long serialVersionUID = -3066996558123931260L;
	
	@Inject
	private FormaPagamentoService formaPagamentoService;
	
	private List<FormaPagamento> formasPagamento;
	
	private FormaPagamento formaPagamentoSelecionado;
	
	private List<FormaPagamento> formasPagamentoSelecionados;
	
	@Override 
	public void init() {
	}
	
	public void btnRemover() {
		try {
			formaPagamentoService.remover(getFormasPagamentoSelecionados());
			getElements().removeAll(getFormasPagamentoSelecionados());
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro(s) removido(s) com sucesso.");
		} catch (NapuleException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao remover. " + e.getMessage());
		}
	}
	
	public void btnSalvar() {
		try {			
			formaPagamentoService.salvar(getEntity());
			setEntity(new FormaPagamento());
			getEstadoViewBean().setUpdate(false);
		}
		catch(NapuleException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar. " + e.getMessage());
		}
	}
	
	public void btnPesquisar() {
		try {	
			setElements(formaPagamentoService.pesquisar(getEntity()));
		}
		catch(NapuleException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_WARN, e.getMessage());
		}
	}
	
	@Override
	protected void beforeSearch() {
		if(getEstadoViewBean().getSearch()) {
			setElements(formaPagamentoService.todos());
		}
		setEntity(new FormaPagamento());
	}

	public List<FormaPagamento> getFormasPagamento() {
		return formasPagamento;
	}

	public void setFormasPagamento(List<FormaPagamento> formasPagamento) {
		this.formasPagamento = formasPagamento;
	}

	public FormaPagamento getFormaPagamentoSelecionado() {
		return formaPagamentoSelecionado;
	}

	public void setFormaPagamentoSelecionado(FormaPagamento formaPagamentoSelecionado) {
		this.formaPagamentoSelecionado = formaPagamentoSelecionado;
	}

	public List<FormaPagamento> getFormasPagamentoSelecionados() {
		return formasPagamentoSelecionados;
	}

	public void setFormasPagamentoSelecionados(List<FormaPagamento> formasPagamentoSelecionados) {
		this.formasPagamentoSelecionados = formasPagamentoSelecionados;
	}
}
