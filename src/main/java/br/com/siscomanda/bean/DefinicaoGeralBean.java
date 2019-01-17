package br.com.siscomanda.bean;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.siscomanda.base.bean.BaseBean;
import br.com.siscomanda.enumeration.ESistema;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.exception.NoImplementationException;
import br.com.siscomanda.model.DefinicaoGeral;
import br.com.siscomanda.service.DefinicaoGeralService;
import br.com.siscomanda.util.JSFUtil;

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
		catch(SiscomandaException e) {
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
