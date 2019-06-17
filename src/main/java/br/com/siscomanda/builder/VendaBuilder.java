package br.com.siscomanda.builder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.siscomanda.enumeration.EStatus;
import br.com.siscomanda.enumeration.ETipoVenda;
import br.com.siscomanda.model.ItemVenda;
import br.com.siscomanda.model.Venda;

public class VendaBuilder implements Serializable {
	
	private static final long serialVersionUID = 2294314144962576823L;
	private Integer numeroPedido;
	private ETipoVenda tipoVenda;
	private EStatus status;
	private String operador;
	private Date dataHora;
	private Double subtotal;
	private Double taxaServico;
	private Double taxaEntrega;
	private Double desconto;
	private Double valorTotal;
	private List<ItemVenda> itens = new ArrayList<ItemVenda>();
	
	public VendaBuilder() {
		this.subtotal = new Double(0);
		this.taxaServico = new Double(0);
		this.taxaEntrega = new Double(0);
		this.desconto = new Double(0);
		this.valorTotal = new Double(0);
	}
	
	public VendaBuilder comNumeroPedido(Integer numeroPedido) {
		this.numeroPedido = numeroPedido;
		return this;
	}
	
	public VendaBuilder comTipoVenda(ETipoVenda tipoVenda) {
		this.tipoVenda = tipoVenda;
		return this;
	}

	public VendaBuilder comStatus(EStatus status) {
		this.status = status;
		return this;
	}
	
	public VendaBuilder comOperador(String operador) {
		this.operador = operador;
		return this;
	}
	
	public VendaBuilder comDataHora(Date dataHora) {
		this.dataHora = dataHora;
		return this;
	}
	
	public VendaBuilder comSubtotal(Double subtotal) {
		this.subtotal = subtotal;
		return this;
	}
	
	public VendaBuilder comTaxaServico(Double taxaServico) {
		this.taxaServico = taxaServico;
		return this;
	}
	
	public VendaBuilder comTaxaEntrega(Double taxaEntrega) {
		this.taxaEntrega = taxaEntrega;
		return this;
	}
	
	public VendaBuilder comDesconto(Double desconto) {
		this.desconto = desconto;
		return this;
	}
	
	public VendaBuilder comItem(ItemVenda item) {
		this.subtotal = item.getValor() * item.getQuantidade();
		this.valorTotal += new Double((subtotal + taxaServico + taxaEntrega) - desconto);
		itens.add(item);
		return this;
	}
	
	public Venda constroi() {
		return new Venda(numeroPedido, tipoVenda, status, operador, dataHora, subtotal, taxaServico, taxaEntrega, desconto, valorTotal, itens);
	}
}
