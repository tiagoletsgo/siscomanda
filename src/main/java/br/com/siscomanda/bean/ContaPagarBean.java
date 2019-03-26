package br.com.siscomanda.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.siscomanda.base.bean.BaseBean;
import br.com.siscomanda.enumeration.EFreaquencia;
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
	
	@Override 
	public void init() {
		novaConta();
	}
	
	private void novaConta() {
		getEntity().setPago(false);
		getEntity().setValor(new Double(0));
		getEntity().setDesconto(new Double(0));
		getEntity().setJuros(new Double(0));
		getEntity().setTotalPago(new Double(0));		
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
	
	public void btnSalvar() {
		try {			
			service.salvar(getEntity(), vencimentos);
			setEntity(new ContaPagar());
			setRepetirConta(false);
			limparVencimentos();
			novaConta();
			
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Conta salva com sucesso.");
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar conta.: " + e.getMessage());
		}
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
}
