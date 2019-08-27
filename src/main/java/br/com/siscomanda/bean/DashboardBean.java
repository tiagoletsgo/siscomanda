package br.com.siscomanda.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.BarChartModel;

import br.com.siscomanda.model.ConfiguracaoGeral;
import br.com.siscomanda.service.ConfiguracaoGeralService;
import br.com.siscomanda.service.DashboardService;
import br.com.siscomanda.service.GraficoEmBarraService;
import br.com.siscomanda.vo.ControladorVO;
import br.com.siscomanda.vo.TotalizadorVO;

@Named
@ViewScoped
public class DashboardBean implements Serializable {

	private static final long serialVersionUID = -9054975930859199146L;
	
	@Inject
	private ConfiguracaoGeralService configuracaoService;
	
	@Inject
	private GraficoEmBarraService graficoEmBarraService;
	
	@Inject
	private DashboardService dashboardService;
	
	private ConfiguracaoGeral configuracao;
	
	private List<ControladorVO> controladores;
	
	private BarChartModel graficoVendaEmBarra;
	
	private TotalizadorVO totalizador;
	
	@PostConstruct
	public void init() {
		configuracao = new ConfiguracaoGeral();
		configuracao = configuracaoService.definicaoSistema();
		atualizaDashboard();
	}
	
	public void atualizaDashboard() {
		controladores = configuracaoService.controladores();
		totalizador = dashboardService.totalizador();
		graficoVendaEmBarra = graficoEmBarraService.gerandoGraficoVenda();
	}
	
	public List<ControladorVO> getControladores() {
		return controladores;
	}
	
	public ConfiguracaoGeral getConfiguracao() {
		return configuracao;
	}
	
	public BarChartModel getGraficoVendaEmBarra() {
		return graficoVendaEmBarra;
	}
	
	public TotalizadorVO getTotalizador() {
		return totalizador;
	}
}
