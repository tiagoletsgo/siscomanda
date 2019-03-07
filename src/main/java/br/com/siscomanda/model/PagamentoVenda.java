package br.com.siscomanda.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.siscomanda.base.model.BaseEntity;

@Entity
@Table(name = "pagamento_venda")
public class PagamentoVenda extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -9027193982094115085L;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "venda_id", nullable = false)
	private Venda venda;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "forma_pagamento_id", nullable = false)
	private FormaPagamento formaPagamento;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bandeira_id", nullable = true)
	private Bandeira bandeira;
		
	@Column(name = "valor_recebido", nullable = false)
	private Double valorRecebido;
	
	@Column(name = "valor_troco", nullable = false)
	private Double valorTroco;
	
	@Column(name = "desconto", nullable = false)
	private Double desconto;
	
	@Column(name = "taxa_servico", nullable = false)
	private Double taxaServico;
	
	@Column(name = "taxa_entrega", nullable = false)
	private Double taxaEntrega;
	
	@Column(name = "valor_total", nullable = false)
	private Double valorTotal;
	
	@Column(name = "valor_pago", nullable = false)
	private Double valorPago;
	
	@Column(name = "valor_venda", nullable = false)
	private Double valorVenda;

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
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

	public Double getValorRecebido() {
		return valorRecebido;
	}

	public void setValorRecebido(Double valorRecebido) {
		this.valorRecebido = valorRecebido;
	}

	public Double getValorTroco() {
		return valorTroco;
	}

	public void setValorTroco(Double valorTroco) {
		this.valorTroco = valorTroco;
	}

	public Double getDesconto() {
		return desconto;
	}

	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}

	public Double getTaxaServico() {
		return taxaServico;
	}

	public void setTaxaServico(Double taxaServico) {
		this.taxaServico = taxaServico;
	}

	public Double getTaxaEntrega() {
		return taxaEntrega;
	}

	public void setTaxaEntrega(Double taxaEntrega) {
		this.taxaEntrega = taxaEntrega;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Double getValorPago() {
		return valorPago;
	}

	public void setValorPago(Double valorPago) {
		this.valorPago = valorPago;
	}

	public Double getValorVenda() {
		return valorVenda;
	}

	public void setValorVenda(Double valorVenda) {
		this.valorVenda = valorVenda;
	}
}
