package br.com.siscomanda.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.siscomanda.base.model.BaseEntity;

@Entity
@Table(name = "caixa_lancamento")
public class CaixaLancamento extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -2358883859884996436L;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "caixa_id", nullable = false)
	private Caixa caixa;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_hora", nullable = false)
	private Date dataHora;
	
	@Column(name = "descricao", nullable = false)
	private String descricao;
	
	@Column(name = "valor_entrada", nullable = false)
	private Double valorEntrada;
	
	@Column(name = "valor_saida", nullable = false)
	private Double valorSaida;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "forma_pagamento_id", nullable = false)
	private FormaPagamento formaPagamento;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bandeira_id", nullable = true)
	private Bandeira bandeira;

	public Caixa getCaixa() {
		return caixa;
	}

	public void setCaixa(Caixa caixa) {
		this.caixa = caixa;
	}

	public Date getDataHora() {
		return dataHora;
	}

	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Double getValorEntrada() {
		return valorEntrada;
	}

	public void setValorEntrada(Double valorEntrada) {
		this.valorEntrada = valorEntrada;
	}

	public Double getValorSaida() {
		return valorSaida;
	}

	public void setValorSaida(Double valorSaida) {
		this.valorSaida = valorSaida;
	}

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
}
