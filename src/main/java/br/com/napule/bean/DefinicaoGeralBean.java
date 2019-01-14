package br.com.napule.bean;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.napule.base.bean.BaseBean;
import br.com.napule.config.jsf.JSFUtil;
import br.com.napule.enumeration.ESistema;
import br.com.napule.exception.NapuleException;
import br.com.napule.exception.NoImplementationException;
import br.com.napule.model.DefinicaoGeral;
import br.com.napule.service.DefinicaoGeralService;

@Named
@ViewScoped
public class DefinicaoGeralBean extends BaseBean<DefinicaoGeral> {
	
	private static final long serialVersionUID = -1667263245728669604L;
	
	@Inject
	private DefinicaoGeralService definicaoGeralService;
	
	@Override
	protected void init() {
		setEntity(definicaoGeralService.carregaDefinicaoSistema());
	}
	
	public void btnSalvar() {
		try {			
			definicaoGeralService.salvar(getEntity());
		}
		catch(NapuleException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar. " + e.getMessage());
		}
	}

	@Override
	protected void beforeSearch() {
		throw new NoImplementationException();
	}
	
	public ESistema[] getSistemas() {
		return ESistema.values();
	}
}
