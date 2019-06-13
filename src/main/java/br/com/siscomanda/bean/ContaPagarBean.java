package br.com.siscomanda.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.siscomanda.base.bean.BaseBean;
import br.com.siscomanda.enumeration.EFreaquencia;
import br.com.siscomanda.enumeration.ESituacaoConta;
import br.com.siscomanda.enumeration.ETipoOperacao;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.ContaPagar;
import br.com.siscomanda.service.ContaPagarService;
import br.com.siscomanda.util.JSFUtil;

@Named
@ViewScoped
public class ContaPagarBean extends BaseBean<ContaPagar> implements Serializable {

	
	private static final long serialVersionUID = 7748885659728861645L;

	@Inject
	private ContaPagarService service;
	
	private boolean repetirConta;
	
	private Integer numeroRepeticao;
	
	private EFreaquencia frequencia;
	
	private List<Date> vencimentos;
	
	private List<ContaPagar> contasSelecionadas;
	
	private ESituacaoConta situacaoConta;
	
	private Date dataVencimentoInicial;
	
	private Date dataVencimentoFinal;
	
	private Date dataPagamentoInicial;
	
	private Date dataPagamentoFinal;
	
	private String descricaoConta;
	
	private boolean contaPaga;
	
	@Override 
	public void init() {
		novaConta();
	}
	
	private void novaConta() {
		getEntity().setPago(false);
		getEntity().setValor(new Double(0));
		getEntity().setDesconto(new Double(0));
		getEntity().setJuros(new Double(0));
		getEntity().setTipoOperacao(ETipoOperacao.DESPESA_CONTA_PAGAR);
	}
	
	public void btnGerar() {
		try {			
			vencimentos = service.gerarVencimento(frequencia, getEntity().getDataVencimento(), numeroRepeticao);
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao gerar vencimentos.: " + e.getMessage());
		}
	}
	
	public void limparVencimentos() {
		if(isNotRepetirConta()) {			
			vencimentos = new ArrayList<>();
			numeroRepeticao = null;
			frequencia = null;
		}
	}
	
	public void btnPesquisar() {
		try {
			Map<String, Object> filter = new HashMap<>();
			filter.put("dataPagamentoInicial", dataPagamentoInicial);
			filter.put("dataPagamentoFinal", dataPagamentoFinal);
			filter.put("dataVencimentoInicial", dataVencimentoInicial);
			filter.put("dataVencimentoFinal", dataVencimentoFinal);
			filter.put("descricaoConta", descricaoConta);
			filter.put("status", situacaoConta);
		
			List<ContaPagar> contas = service.buscaPorFiltro(filter);
			setElements(contas);
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao fazer pesquisa.: " + e.getMessage());
		}
	}
	
	public void btnRemover() {
		try {
			service.remover(getContasSelecionadas());
			getElements().removeAll(getContasSelecionadas());
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro(s) removido(s) com sucesso.");
		} catch (SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao remover. " + e.getMessage());
		}
	}
	
	public void btnSalvar() {
		try {			
			service.salvar(getEntity(), vencimentos, contaPaga);
			setEntity(new ContaPagar());
			setRepetirConta(false);
			limparVencimentos();
			novaConta();
			
			contaPaga = false;
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar conta.: " + e.getMessage());
		}
	}
	
	@Override
	public void btnNovo() {
		setEntity(new ContaPagar());
		super.btnNovo();
		novaConta();		
	}
	
	@Override
	public void btnCancelar() {
		setContaPaga(false);
		super.btnCancelar();
	}

	@Override
	protected void beforeSearch() {
	}
	
	public Integer getNumeroRepeticao() {
		return numeroRepeticao;
	}

	public void setNumeroRepeticao(Integer numeroRepeticao) {
		this.numeroRepeticao = numeroRepeticao;
	}

	public EFreaquencia getFrequencia() {
		return frequencia;
	}

	public void setFrequencia(EFreaquencia frequencia) {
		this.frequencia = frequencia;
	}

	public boolean isRepetirConta() {
		return repetirConta;
	}

	public void setRepetirConta(boolean repetirConta) {
		this.repetirConta = repetirConta;
	}

	public EFreaquencia[] getFrequencias() {
		return EFreaquencia.values();
	}
	
	public boolean isNotRepetirConta() {
		return !isRepetirConta();
	}
	
	public List<Date> getVencimentos() {
		return vencimentos;
	}

	public List<ContaPagar> getContasSelecionadas() {
		return contasSelecionadas;
	}

	public void setContasSelecionadas(List<ContaPagar> contasSelecionadas) {
		this.contasSelecionadas = contasSelecionadas;
	}
	
	public ESituacaoConta[] getSituacoes() {
		return ESituacaoConta.values();
	}

	public ESituacaoConta getSituacaoConta() {
		return situacaoConta;
	}

	public void setSituacaoConta(ESituacaoConta situacaoConta) {
		this.situacaoConta = situacaoConta;
	}

	public Date getDataVencimentoInicial() {
		return dataVencimentoInicial;
	}

	public void setDataVencimentoInicial(Date dataVencimentoInicial) {
		this.dataVencimentoInicial = dataVencimentoInicial;
	}

	public Date getDataVencimentoFinal() {
		return dataVencimentoFinal;
	}

	public void setDataVencimentoFinal(Date dataVencimentoFinal) {
		this.dataVencimentoFinal = dataVencimentoFinal;
	}

	public Date getDataPagamentoInicial() {
		return dataPagamentoInicial;
	}

	public void setDataPagamentoInicial(Date dataPagamentoInicial) {
		this.dataPagamentoInicial = dataPagamentoInicial;
	}

	public Date getDataPagamentoFinal() {
		return dataPagamentoFinal;
	}

	public void setDataPagamentoFinal(Date dataPagamentoFinal) {
		this.dataPagamentoFinal = dataPagamentoFinal;
	}

	public String getDescricaoConta() {
		return descricaoConta;
	}

	public void setDescricaoConta(String descricaoConta) {
		this.descricaoConta = descricaoConta;
	}

	public boolean isContaPaga() {
		return contaPaga;
	}

	public void setContaPaga(boolean contaPaga) {
		this.contaPaga = contaPaga;
	}
}
