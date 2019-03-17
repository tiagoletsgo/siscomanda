package br.com.siscomanda.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.siscomanda.base.bean.BaseBean;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Caixa;
import br.com.siscomanda.service.CaixaService;
import br.com.siscomanda.util.JSFUtil;

@Named
@ViewScoped
public class CaixaBean extends BaseBean<Caixa> implements Serializable {

	private static final long serialVersionUID = -5551019343597489328L;
	
	@Inject
	private CaixaService service;
	
	@Override
	protected void init() {
		setEntity(service.initCaixa());
	}
	
	public void btnAbrirCaixa() {
		try {
			service.abrirCaixa(getEntity());
			getEntity().getLancamentos().addAll(service.lancamentos());
			
			Map<String, Object> totalizadores = service.calculaTotalizador(getEntity().getLancamentos());
			getEntity().setTotalEntrada((Double)totalizadores.get("TOTAL_ENTRADA"));
			getEntity().setTotalSaida((Double)totalizadores.get("TOTAL_SAIDA"));
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Caixa aberto com sucesso.");
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao tentar abrir caixa. " + e.getMessage());
		}
	}
	
	public void btnFecharCaixa() {
		getEntity().setDataHoraFechamento(new Date());
		getEntity().setCaixaAberto(false);
	}
	
	public void valorTotalEntrada() {
		
	}
	
	@Override
	protected void beforeSearch() {
	}	
}
