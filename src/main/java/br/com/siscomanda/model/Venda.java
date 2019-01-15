package br.com.siscomanda.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.siscomanda.base.model.BaseEntity;
import br.com.siscomanda.enumeration.EStatus;

public class Venda extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 2583564472683970706L;
	
	private FormaPagamento formaPagamento;
	
	private List<ItemVenda> itens = new ArrayList<>();
	
	private boolean pago;
	
	private EStatus status;
	
	private Cliente cliente;
	
	private Date iniciado;
	
	private Date finalizado;
	
	private double total;
	
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

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public int getMesaOuComanda() {
		return mesaOuComanda;
	}

	public void setMesaOuComanda(int mesaOuComanda) {
		this.mesaOuComanda = mesaOuComanda;
	}
}
