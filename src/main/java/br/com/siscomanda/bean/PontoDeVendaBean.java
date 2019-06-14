package br.com.siscomanda.bean;

import java.io.Serializable;

import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import br.com.siscomanda.base.bean.BaseBean;
import br.com.siscomanda.model.Venda;

@Named
@ViewScoped
public class PontoDeVendaBean extends BaseBean<Venda> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	protected void init() {
	}

	@Override
	protected void beforeSearch() {
	}
}
