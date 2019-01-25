package br.com.siscomanda.bean;

import java.io.Serializable;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.siscomanda.base.bean.BaseBean;
import br.com.siscomanda.enumeration.EFatorMedida;
import br.com.siscomanda.model.Adicional;
import br.com.siscomanda.service.AdicionalService;

@Named
@ViewScoped
public class AdicionalBean extends BaseBean<Adicional> implements Serializable {

	private static final long serialVersionUID = 8194647047375428193L;
	
	@Inject
	private AdicionalService service;
	
	@Override
	protected void init() {
		getEntity().setPrecoVenda(new Double("0"));
		getEntity().setPrecoCusto(new Double("0"));
		getEntity().setFatorMedida(EFatorMedida.UN);
	}

	@Override
	protected void beforeSearch() {
	}
	
	public EFatorMedida[] getFatorMedida() {
		return EFatorMedida.values();
	}
}
