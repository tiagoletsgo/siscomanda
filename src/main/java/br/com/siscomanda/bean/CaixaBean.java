package br.com.siscomanda.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.siscomanda.base.bean.BaseBean;
import br.com.siscomanda.enumeration.ETipoOperacao;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Caixa;
import br.com.siscomanda.model.CaixaLancamento;
import br.com.siscomanda.model.FormaPagamento;
import br.com.siscomanda.service.CaixaService;
import br.com.siscomanda.util.JSFUtil;

@Named
@ViewScoped
public class CaixaBean extends BaseBean<Caixa> implements Serializable {

	private static final long serialVersionUID = -5551019343597489328L;
	
	@Inject
	private CaixaService service;
	
	private List<FormaPagamento> formaPagamentos;
	
	private CaixaLancamento lancamento;
	
	private Double valor;
	
	private Double totalDinheiro;
	
	private String opcaoPesquisa;
	
	private Integer ultimosDias;
	
	private Date dataDe;
	
	private Date dataA;
		
	@Override
	protected void init() {
		formaPagamentos = service.buscaFormaPagamentoPorDescricao("DINHEIRO");
		lancamento = new CaixaLancamento();
		valor = new Double(0);
		totalDinheiro = new Double(0);
		
		setEntity(service.initCaixa());
		setEntity(service.temCaixaAberto());
		setOpcaoPesquisa("EXIBIR");
		atualizaTabelaLancamento();
		executaCalculaTotalizador();
	}
	
	public void btnAbrirCaixa() {
		try {
			service.abrirCaixa(getEntity());
			atualizaTabelaLancamento();
			executaCalculaTotalizador();
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Caixa aberto com sucesso.");
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao tentar abrir caixa. " + e.getMessage());
		}
	}
	
	private void executaCalculaTotalizador() {
		Map<String, Object> totalizadores = service.calculaTotalizador(getEntity().getLancamentos());
		getEntity().setTotalEntrada((Double)totalizadores.get("TOTAL_ENTRADA"));
		getEntity().setTotalSaida((Double)totalizadores.get("TOTAL_SAIDA"));
		totalDinheiro = (Double)totalizadores.get("TOTAL_DINHEIRO");
	}
	
	@Override
	public void btnNovo() {
		setEntity(new Caixa());
		super.btnNovo();
		init();
	}
	
	public void btnVer() {
		super.btnNovo();
	}
	
	public void btnFecharCaixa() {
		try {
			service.fecharCaixa(getEntity());
			setEntity(service.initCaixa());
			
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Caixa fechado com sucesso.");
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro fechamento do caixa. " + e.getMessage());
		}
	}
	
	public void adicionaEntradaSaida() {
		try {
			lancamento.setCaixa(getEntity());
			lancamento = service.adicionaEntradaSaida(lancamento, valor, totalDinheiro);
			getEntity().getLancamentos().add(lancamento);
			atualizaTabelaLancamento();
			executaCalculaTotalizador();
			
			lancamento = new CaixaLancamento();
			valor = new Double(0);
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Lançamento incluido com sucesso.");
		}catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar lançamento. " + e.getMessage());
		}
	}
	
	public void btnDetalhar(Caixa caixa) {
		caixa = service.detalhar(caixa);
		setEntity(caixa);

		atualizaTabelaLancamento();
		executaCalculaTotalizador();
		
		super.btnEditar(getEntity());
	}
	
	public void removerLancamento() {
		try {			
			service.removeLancamento(getLancamento());
			getEntity().getLancamentos().remove(getLancamento());
			atualizaTabelaLancamento();
			executaCalculaTotalizador();
			
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Lançamento removido com sucesso.");
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro remover lançamento. " + e.getMessage());
		}
	}
	
	public void btnPesquisar() {
		try {
			setElements(service.pesquisar(getUltimosDias(), getDataDe(), getDataA()));
			setDataA(null);
			setDataDe(null);
			setUltimosDias(null);
		}catch(SiscomandaException e) {
			setElements(new ArrayList<>());
			if(e.getMessage().contains("Selecione")) {
				JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao tentar pesquisar caixas.: " + e.getMessage());
				return;
			}
			JSFUtil.addMessage(FacesMessage.SEVERITY_WARN, "Atenção.: " + e.getMessage());
		}
	}
	
	private void atualizaTabelaLancamento() {
		getEntity().setLancamentos(service.lancamentos(getEntity()));
		Collections.sort(getEntity().getLancamentos());
	}
	
	@Override
	protected void beforeSearch() {
	
	}	
	
	public List<FormaPagamento> getFormaPagamentos() {
		return formaPagamentos;
	}
	
	public List<ETipoOperacao> getTipoOperacao() {
		return service.operacoes();
	}

	public CaixaLancamento getLancamento() {
		return lancamento;
	}

	public void setLancamento(CaixaLancamento lancamento) {
		this.lancamento = lancamento;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}
	
	public void setTotalDinheiro(Double totalDinheiro) {
		this.totalDinheiro = totalDinheiro;
	}
	
	public Double getTotalDinheiro() {
		return totalDinheiro;
	}

	public String getOpcaoPesquisa() {
		return opcaoPesquisa;
	}

	public void setOpcaoPesquisa(String opcaoPesquisa) {
		this.opcaoPesquisa = opcaoPesquisa;
	}

	public Integer getUltimosDias() {
		return ultimosDias;
	}

	public void setUltimosDias(Integer ultimosDias) {
		this.ultimosDias = ultimosDias;
	}

	public Date getDataDe() {
		return dataDe;
	}

	public void setDataDe(Date dataDe) {
		this.dataDe = dataDe;
	}

	public Date getDataA() {
		return dataA;
	}

	public void setDataA(Date dataA) {
		this.dataA = dataA;
	}
}
