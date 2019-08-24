package br.com.siscomanda.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.siscomanda.model.ConfiguracaoGeral;
import br.com.siscomanda.service.ConfiguracaoGeralService;
import br.com.siscomanda.vo.ControladorVO;

@Named
@ViewScoped
public class DashboardBean implements Serializable {

	private static final long serialVersionUID = -9054975930859199146L;
	
	@Inject
	private ConfiguracaoGeralService configuracaoService;
	
	private ConfiguracaoGeral configuracao;
	
	private List<ControladorVO> controladores;
	
	@PostConstruct
	public void init() {
		configuracao = new ConfiguracaoGeral();
		configuracao = configuracaoService.definicaoSistema();
		controladores = configuracaoService.controladores();
	}
	
	public void atualizaPainelControladores() {
		controladores = configuracaoService.controladores();
	}
	
	public List<ControladorVO> getControladores() {
		return controladores;
	}
	
	public ConfiguracaoGeral getConfiguracao() {
		return configuracao;
	}
}
