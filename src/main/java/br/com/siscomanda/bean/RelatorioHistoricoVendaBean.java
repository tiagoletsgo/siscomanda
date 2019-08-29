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

import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.service.PontoDeVendaService;
import br.com.siscomanda.util.JSFUtil;
import br.com.siscomanda.vo.HistoricoVendaVO;

@Named
@ViewScoped
public class RelatorioHistoricoVendaBean implements Serializable {

	private static final long serialVersionUID = 4055775123574692889L;

	private List<HistoricoVendaVO> historicos;
	private Map<String, Object> filter = new HashMap<>();;

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
		}
		catch(SiscomandaException e) {
			this.historicos = new ArrayList<>();
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}
	
	public List<HistoricoVendaVO> getHistoricos() {
		return historicos;
	}

	public Map<String, Object> getFilter() {
		return filter;
	}	
}
