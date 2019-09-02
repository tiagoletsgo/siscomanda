package br.com.siscomanda.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.siscomanda.base.Relatorio;
import br.com.siscomanda.enumeration.ETipoRelatorio;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.service.PontoDeVendaService;
import br.com.siscomanda.util.JSFUtil;
import br.com.siscomanda.vo.HistoricoVendaVO;

@Named
@ViewScoped
public class RelatorioHistoricoVendaBean extends Relatorio implements Serializable {

	private static final long serialVersionUID = 4055775123574692889L;

	private List<HistoricoVendaVO> historicos;
	private List<HistoricoVendaVO> selecionados;
	private Map<String, Object> filter = new HashMap<>();

	@Inject
	private PontoDeVendaService pontoDeVendaService;

	@PostConstruct
	public void init() {
		this.filter.put("dataInicial", null);
		this.filter.put("dataFinal", null);
		this.filter.put("nomeCliente", null);
		this.filter.put("nomeFuncionario", null);
		this.filter.put("status", "TODOS");
		
		this.filter.put("totalDeEntrega", 0D);
		this.filter.put("totalDeServico", 0D);
		this.filter.put("totalReceber", 0D);
		this.filter.put("totalGeral", 0D);
	}

	public void btnPesquisar() {
		try {
			this.historicos = pontoDeVendaService.historicoVenda(filter);
			recalcularTotalizador();
		}
		catch(SiscomandaException e) {
			this.historicos = new ArrayList<>();
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}
	
	public void recalcularTotalizador() {
		
		List<HistoricoVendaVO> vendas = selecionados != null && !this.selecionados.isEmpty() ? this.selecionados : this.historicos;
		
		double totalDeEntrega = 0D;
		double totalDeServico = 0D;
		double totalReceber = 0D;
		
		for(HistoricoVendaVO venda : vendas) {
			totalDeEntrega += venda.getEntrega();
			totalDeServico += venda.getServico();
			totalReceber += venda.getTotal();
		}
		
		filter.put("totalDeEntrega", totalDeEntrega);
		filter.put("totalDeServico", totalDeServico);
		filter.put("totalReceber", totalReceber);
		filter.put("totalGeral", (totalReceber + totalDeServico + totalDeEntrega));
	}
	
	public void gerarRelatorioPDF() {
		try {
			gerarRelatorio(ETipoRelatorio.PDF);
		} catch (SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}
	
	public void gerarRelatorioXLS() {
		try {
			gerarRelatorio(ETipoRelatorio.XLS);
		} catch (SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}
	
	public List<HistoricoVendaVO> getHistoricos() {
		return historicos;
	}

	public Map<String, Object> getFilter() {
		return filter;
	}

	@Override
	public String pathJasper() {
//		return "/report/historicoVendaPDF.jasper";
		return "/report/test.jasper";
	}

	@Override
	public String nomeRelatorio() {
		return "HistoricoVenda";
	}

	@Override
	public List<?> colecaoParaGerarRelatorio() {
		return this.historicos;
	}
	
	@Override
	public List<?> colecaoDeDadosSelecionados() {
		return this.selecionados;
	}

	@Override
	public Map<String, Object> parametros() {
		return null;
	}

	public List<HistoricoVendaVO> getSelecionados() {
		return selecionados;
	}

	public void setSelecionados(List<HistoricoVendaVO> selecionados) {
		this.selecionados = selecionados;
	}
}
