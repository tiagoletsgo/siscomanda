package br.com.siscomanda.service;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import br.com.siscomanda.enumeration.EStatus;
import br.com.siscomanda.model.Venda;
import br.com.siscomanda.repository.dao.VendaDAO;
import br.com.siscomanda.util.DateUtil;

public class GraficoEmBarraService implements Serializable {

	private static final long serialVersionUID = -3506195382173392334L;
	
	@Inject
	private VendaDAO vendaDAO;
	
	private BarChartModel initChartBarModel() {
		BarChartModel model = new BarChartModel();
		List<Venda> vendas = vendaDAO.porData(DateUtil.primeiroDiaDoMesAtual(), DateUtil.ultimoDiaDoMesAtual());
		
		ChartSeries series = new ChartSeries();
		series.setLabel(DateUtil.extrairMesPorExtenso(new Date()));
		
		for(int i = 1; i <= DateUtil.totalDiasDoMes(new Date()); i++) {	
			double valor = 0D;
			for(Venda venda : vendas) {
				if(DateUtil.extrairDia(venda.getDataVenda()) == new Integer(i)
						&& !venda.getStatus().equals(EStatus.CANCELADO)) {
					valor += venda.getTotal();
				}
			}
			series.set(i, valor);
		}
		
		model.addSeries(series);
		return model;
	}
	
	public BarChartModel gerandoGraficoVenda() {
		BarChartModel chart = initChartBarModel();
		
		chart.setTitle("Gráfico de Venda Diário");
		chart.setAnimate(true);
		chart.setLegendPosition("ne");
		chart.setShowPointLabels(true);
		chart.setShowDatatip(false);
		Axis yAxis = chart.getAxis(AxisType.Y);
		yAxis.setTickFormat("%'.2f");
        yAxis.setMin(0);
        yAxis.setMax(2500);
		
		return chart;
	}
}
