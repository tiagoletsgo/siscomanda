package br.com.siscomanda.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.siscomanda.base.model.BaseEntity;
import br.com.siscomanda.enumeration.EStatus;

@Entity
@Table(name = "venda")
public class Venda extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 2583564472683970706L;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "form_pagamento_id", nullable = true)
	private FormaPagamento formaPagamento;
	
	@OneToMany(mappedBy = "venda", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ItemVenda> itens = new ArrayList<>();
	
	@Column(name = "pago", nullable = false)
	private boolean pago;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private EStatus status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cliente_id", nullable = true)
	private Cliente cliente;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_iniciado", nullable = false)
	private Date iniciado;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_finalizado", nullable = true)
	private Date finalizado;
	
	@Column(name = "subtotal", nullable = false)
	private Double subtotal;
	
	@Column(name = "taxa_servico", nullable = false)
	private Double taxaServico;
	
	@Column(name = "taxa_entrega", nullable = false)
	private Double taxaEntrega;
	
	@Column(name = "desconto", nullable = false)
	private Double desconto;

//	private Usuario operador
	
	@Column(name = "total", nullable = false)
	private Double total;
	
	@Column(name = "valor_pago", nullable = false)
	private Double valorPago;
	
	@Column(name = "mesa_comanda", nullable = false)
	private int mesaOuComanda;

	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public List<ItemVenda> getItens() {
		return itens;
	}

	public void setItens(List<ItemVenda> itens) {
		this.itens = itens;
	}

	public boolean isPago() {
		return pago;
	}

	public void setPago(boolean pago) {
		this.pago = pago;
	}

	public EStatus getStatus() {
		return status;
	}

	public void setStatus(EStatus status) {
		this.status = status;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Date getIniciado() {
		return iniciado;
	}

	public void setIniciado(Date iniciado) {
		this.iniciado = iniciado;
	}

	public Date getFinalizado() {
		return finalizado;
	}

	public void setFinalizado(Date finalizado) {
		this.finalizado = finalizado;
	}

	public Double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

	public Double getTaxaServico() {
		return taxaServico;
	}

	public void setTaxaServico(Double taxaServico) {
		this.taxaServico = taxaServico;
	}

	public Double getDesconto() {
		return desconto;
	}

	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public int getMesaOuComanda() {
		return mesaOuComanda;
	}

	public void setMesaOuComanda(int mesaOuComanda) {
		this.mesaOuComanda = mesaOuComanda;
	}

	public Double getTaxaEntrega() {
		return taxaEntrega;
	}

	public void setTaxaEntrega(Double taxaEntrega) {
		this.taxaEntrega = taxaEntrega;
	}

	public Double getValorPago() {
		return valorPago;
	}

	public void setValorPago(Double valorPago) {
		this.valorPago = valorPago;
	}
}
