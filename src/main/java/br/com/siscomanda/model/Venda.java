package br.com.siscomanda.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Transient;

import br.com.siscomanda.base.model.BaseEntity;
import br.com.siscomanda.enumeration.EStatus;
import br.com.siscomanda.enumeration.ETipoVenda;

public class Venda extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1323965645259119303L;

	private Integer numeroPedido;
	private ETipoVenda tipoVenda;
	private EStatus status;
	private String operador;
	private Date dataHora;
	private Double subtotal;
	private Double taxaServico;
	private Double taxaEntrega;
	private Double desconto;
	private Double total;
	private Integer controle;
	private List<ItemVenda> itens = new ArrayList<ItemVenda>();
	
	public Venda() {	}
	
	public Venda(Integer numeroPedido, ETipoVenda tipoVenda, EStatus status, String operador, Date dataHora,
			Double subtotal, Double taxaServico, Double taxaEntrega, Double desconto, Double total, List<ItemVenda> itens, Integer controle) {
		
		this.numeroPedido = numeroPedido;
		this.tipoVenda = tipoVenda;
		this.status = status;
		this.operador = operador;
		this.dataHora = dataHora;
		this.subtotal = subtotal;
		this.taxaServico = taxaServico;
		this.taxaEntrega = taxaEntrega;
		this.desconto = desconto;
		this.total = total;
		this.controle = controle;
		this.itens = itens;
	}

	public Integer getNumeroPedido() {
		return numeroPedido;
	}

	public void setNumeroPedido(Integer numeroPedido) {
		this.numeroPedido = numeroPedido;
	}

	public ETipoVenda getTipoVenda() {
		return tipoVenda;
	}

	public void setTipoVenda(ETipoVenda tipoVenda) {
		this.tipoVenda = tipoVenda;
	}

	public EStatus getStatus() {
		return status;
	}

	public void setStatus(EStatus status) {
		this.status = status;
	}

	public String getOperador() {
		return operador;
	}

	public void setOperador(String operador) {
		this.operador = operador;
	}

	public Date getDataHora() {
		return dataHora;
	}

	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
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

	public Double getTaxaEntrega() {
		return taxaEntrega;
	}

	public void setTaxaEntrega(Double taxaEntrega) {
		this.taxaEntrega = taxaEntrega;
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

	public Integer getControle() {
		return controle;
	}

	public void setControle(Integer controle) {
		this.controle = controle;
	}

	public List<ItemVenda> getItens() {
		return itens;
	}

	public void setItens(List<ItemVenda> itens) {
		this.itens = itens;
	}
	
	@Transient
	public boolean isBloqueiaVendaMesaOuComanda() {
		
		if(Objects.nonNull(getTipoVenda()) && getTipoVenda().equals(ETipoVenda.MESA) || 
				Objects.nonNull(getTipoVenda()) && getTipoVenda().equals(ETipoVenda.COMANDA)) {
			return true;
		}
		
		return false;
	}
	
	@Transient
	public boolean isNotBloqueiaVendaMesaOuComanda() {
		return !isBloqueiaVendaMesaOuComanda();
	}
	
	@Transient
	public boolean isBloqueiaVendaDelivery() {
		
		if(Objects.nonNull(getTipoVenda()) && getTipoVenda().equals(ETipoVenda.DELIVERY)) {
			return true;
		}
		
		return false;
	}
	
	@Transient
	public boolean isNotBloqueiaVendaDelivery() {
		return !isBloqueiaVendaDelivery();
	}
}
