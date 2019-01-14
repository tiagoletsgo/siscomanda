package br.com.napule.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.napule.base.model.BaseEntity;

@Entity
@Table(name = "vincula_forma_pagamento")
public class VinculaFormaPagamento extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 2782407759490083337L;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "forma_pagamento_id", nullable = false)
	private FormaPagamento formaPagamento;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bandeira_id", nullable = false)
	private Bandeira bandeira;
		
	@Column(name = "taxa", nullable = false)
	private double taxa;
	
	@Column(name = "dias_para_receber", nullable = false)
	private int diasParaReceber;
	
	@Column(name = "vincular", nullable = false)
	private boolean vincular;

	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public Bandeira getBandeira() {
		return bandeira;
	}

	public void setBandeira(Bandeira bandeira) {
		this.bandeira = bandeira;
	}

	public double getTaxa() {
		return taxa;
	}

	public void setTaxa(double taxa) {
		this.taxa = taxa;
	}

	public int getDiasParaReceber() {
		return diasParaReceber;
	}

	public void setDiasParaReceber(int diasParaReceber) {
		this.diasParaReceber = diasParaReceber;
	}

	public boolean isVincular() {
		return vincular;
	}

	public void setVincular(boolean vincular) {
		this.vincular = vincular;
	}
}
