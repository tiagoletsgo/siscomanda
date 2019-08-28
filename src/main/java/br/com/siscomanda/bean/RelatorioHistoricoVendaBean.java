package br.com.siscomanda.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.siscomanda.service.PontoDeVendaService;
import br.com.siscomanda.vo.HistoricoVendaVO;

@Named
@ViewScoped
public class RelatorioHistoricoVendaBean implements Serializable {

	private static final long serialVersionUID = 4055775123574692889L;

	private List<HistoricoVendaVO> historicos;

	private Date dataInicial;
	private Date dataFinal;
	private String nomeFuncionario;
	private String nomeCliente;
	private String opcoesPesquisa;

	@Inject
	private PontoDeVendaService pontoDeVendaService;

	@PostConstruct
	public void init() {
		this.opcoesPesquisa = "TODOS";
		this.historicos = pontoDeVendaService.historicoVenda(new HashMap<>());
	}

	public List<HistoricoVendaVO> getHistoricos() {
		return historicos;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public String getNomeFuncionario() {
		return nomeFuncionario;
	}

	public void setNomeFuncionario(String nomeFuncionario) {
		this.nomeFuncionario = nomeFuncionario;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public String getOpcoesPesquisa() {
		return opcoesPesquisa;
	}

	public void setOpcoesPesquisa(String opcoesPesquisa) {
		this.opcoesPesquisa = opcoesPesquisa;
	}
}
