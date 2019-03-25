package br.com.siscomanda.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.com.siscomanda.base.model.BaseEntity;
import br.com.siscomanda.enumeration.ETipoOperacao;

@Entity
@Table(name = "caixa_lancamento")
public class CaixaLancamento extends BaseEntity implements Serializable, Comparable<CaixaLancamento> {

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
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_operacao", nullable = false)
	private ETipoOperacao tipoOperacao;

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

	public ETipoOperacao getTipoOperacao() {
		return tipoOperacao;
	}

	public void setTipoOperacao(ETipoOperacao tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}
	
	@Transient
	public boolean isVenda() {
		return getTipoOperacao().equals(ETipoOperacao.VENDA);
	}
	
	@Transient
	public boolean isNotVenda() {
		return !isVenda();
	}
	
	@Transient
	public boolean isCaixaAberto() {
		return getCaixa().getCaixaAberto();
	}
	
	@Transient
	public boolean isNotCaixaAberto() {
		return !isCaixaAberto();
	}

	@Override
	public int compareTo(CaixaLancamento o) {
		if(o.getId() != null && getId() != null) {
			if(getId() < o.getId()) {
				return -1;
			}
		}
				
		return 0;
	}
}
