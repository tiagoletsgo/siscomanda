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
import br.com.napule.model.FormaPagamento;
import br.com.napule.model.VinculaFormaPagamento;
import br.com.napule.service.BandeiraService;
import br.com.napule.service.FormaPagamentoService;
import br.com.napule.service.VinculaFormaPagamentoService;

@Named
@ViewScoped
public class VinculaFormaPagamentoBean extends BaseBean<VinculaFormaPagamento> implements Serializable {

	private static final long serialVersionUID = 1218245540865820122L;
	
	@Inject
	private FormaPagamentoService formaPagamentoService;
	
	@Inject
	private BandeiraService bandeiraService;
	
	@Inject
	private VinculaFormaPagamentoService vinculaFormaPagamentoService;
	
	private FormaPagamento formaPagamentoSelecionado;
	
	private List<FormaPagamento> formasPagamento;
	
	private List<Bandeira> bandeiras;
	
	private boolean disabled;
	
	@Override
	protected void init() {
		formasPagamento = formaPagamentoService.todos();
		bandeiras = bandeiraService.todos();
		
		setElements(vinculaFormaPagamentoService.vinculaFormasPagamento(bandeiras, null));
		disabled = true;
	}
	
	public void btnSalvar() {
		try {
			vinculaFormaPagamentoService.salvar(getElements(), formaPagamentoSelecionado);
		}
		catch(NapuleException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar. " + e.getMessage());
		}
	}
	
	public void desbloqueiaTabelaBandeira() {
		if("DINHEIRO".equals(formaPagamentoSelecionado.getDescricao())) {
			setElements(vinculaFormaPagamentoService.vinculaFormasPagamento(bandeiras, null));
			disabled = true;
			return;
		}
		
		setElements(vinculaFormaPagamentoService.vinculaFormasPagamento(bandeiras, formaPagamentoSelecionado));
		disabled = false;
	}

	@Override
	protected void beforeSearch() {
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public FormaPagamento getFormaPagamentoSelecionado() {
		return formaPagamentoSelecionado;
	}

	public void setFormaPagamentoSelecionado(FormaPagamento formaPagamentoSelecionado) {
		this.formaPagamentoSelecionado = formaPagamentoSelecionado;
	}
	
	public List<FormaPagamento> getFormasPagamento() {
		return formasPagamento;
	}
	
	public List<Bandeira> getBandeiras() {
		return bandeiras;
	}
}
