package br.com.siscomanda.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.com.siscomanda.base.model.BaseEntity;
import br.com.siscomanda.enumeration.ETipoOperacao;

@Entity
@Table(name = "conta_pagar")
public class ContaPagar extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 9086450591305182894L;
	
	@Column(name = "descricao", nullable = false)
	private String descricao;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "data_pagamento")
	private Date dataPagamento;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "data_vencimento", nullable = false)
	private Date dataVencimento;
	
	@Column(name = "conta_paga")
	private Boolean pago;
	
	@Column(name = "valor", nullable = false)
	private Double valor;
	
	@Column(name = "juros")
	private Double juros;
	
	@Column(name = "desconto")
	private Double desconto;
	
	@Column(name = "total_pago", nullable = false)
	private Double totalPago;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_operacao", nullable = false)
	private ETipoOperacao tipoOperacao;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Boolean getPago() {
		return pago;
	}

	public void setPago(Boolean pago) {
		this.pago = pago;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Double getJuros() {
		return juros;
	}

	public void setJuros(Double juros) {
		this.juros = juros;
	}

	public Double getDesconto() {
		return desconto;
	}

	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}

	public Double getTotalPago() {
		totalPago = (getValor() + getJuros()) - getDesconto();
		return totalPago;
	}

	public void setTotalPago(Double totalPago) {
		this.totalPago = totalPago;
	}
	
	public ETipoOperacao getTipoOperacao() {
		return tipoOperacao;
	}

	public void setTipoOperacao(ETipoOperacao tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}

	@Transient
	public boolean isPaga() {
		return getPago() == true && getValor().equals(getTotalPago())
				&& !isNovo() || getPago() == true && getTotalPago() <= getValor() && !isNovo()
				|| getPago() == true && getTotalPago() >= getValor() && !isNovo();
	}
	
	@Transient
	public boolean isNotPago() {
		return !isPaga();
	}
}
