package br.com.siscomanda.bean;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.siscomanda.base.bean.BaseBean;
import br.com.siscomanda.enumeration.ESistema;
import br.com.siscomanda.exception.NoImplementationException;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.ConfiguracaoGeral;
import br.com.siscomanda.service.ConfiguracaoGeralService;
import br.com.siscomanda.util.JSFUtil;

@Named
@ViewScoped
public class ConfiguracaoGeralBean extends BaseBean<ConfiguracaoGeral> {
	
	private static final long serialVersionUID = -1667263245728669604L;
	
	@Inject
	private ConfiguracaoGeralService configuracaoGeralService;
	
	@Override
	protected void init() {
		setEntity(configuracaoGeralService.carregaDefinicaoSistema());
	}
	
	public void btnSalvar() {
		try {			
			configuracaoGeralService.salvar(getEntity());
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
