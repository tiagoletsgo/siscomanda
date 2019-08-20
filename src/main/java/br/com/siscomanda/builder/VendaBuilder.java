package br.com.siscomanda.builder;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import br.com.siscomanda.enumeration.EStatus;
import br.com.siscomanda.enumeration.ETipoVenda;
import br.com.siscomanda.model.Cliente;
import br.com.siscomanda.model.ItemVenda;
import br.com.siscomanda.model.Usuario;
import br.com.siscomanda.model.Venda;

public class VendaBuilder implements Serializable {
	
	private static final long serialVersionUID = 2294314144962576823L;
	private Venda venda;
	
	public VendaBuilder(Venda venda) {
		this.venda = venda;
		this.venda.setSubtotal(new Double(0));
		this.venda.setTaxaServico(new Double(0));
		this.venda.setTaxaEntrega(new Double(0));
		this.venda.setDesconto(new Double(0));
		this.venda.setTotal(new Double(0));
		this.venda.setControle(new Integer(0));
		this.venda.setValorPago(new Double(0));
		this.venda.setStatus(EStatus.EM_ABERTO);
		this.venda.setDataHora(new Date());
		this.venda.setDataVenda(new Date());
		this.venda.setPago(false);
		this.venda.setOperador(new Usuario());
		this.venda.setFatorCalculoTaxaServico(new Double(0));
//		this.venda.setCliente(new Cliente());
	}
	
	public VendaBuilder comNumeroVenda(Long numeroVenda) {
		this.venda.setId(numeroVenda);
		return this;
	}
	
	public VendaBuilder comControle(Integer numeroControle) {
		this.venda.setControle(numeroControle);
		return this;
	}
	
	public VendaBuilder comTipoVenda(ETipoVenda tipoVenda) {
		this.venda.setTipoVenda(tipoVenda);
		return this;
	}

	public VendaBuilder comStatus(EStatus status) {
		boolean pago = false;
		
		if(status.equals(EStatus.PAGO)) {
			pago = true;
		}
		
		this.venda.setPago(pago);
		this.venda.setStatus(status);
		return this;
	}
	
	public VendaBuilder comValorPago(Double valorPago) {
		this.venda.setValorPago(valorPago);
		return this;
	}
	
	public VendaBuilder comCliente(Cliente cliente) {
		this.venda.setCliente(cliente);
		return this;
	}
	
	public VendaBuilder comOperador(Usuario operador) {
		this.venda.setOperador(operador);
		return this;
	}
	
	public VendaBuilder comDataHora(Date dataHora) {
		this.venda.setDataHora(dataHora);
		return this;
	}
	
	public VendaBuilder comTaxaServico(Double taxa) {
		this.venda.setFatorCalculoTaxaServico(taxa);
		return this;
	}
	
	public VendaBuilder comTaxaEntrega(Double taxaEntrega) {
		this.venda.setTaxaEntrega(taxaEntrega);
		return this;
	}
	
	public VendaBuilder comDesconto(Double desconto) {
		this.venda.setDesconto(desconto);
		return this;
	}
	
	public VendaBuilder comItens(List<ItemVenda> itens) {
		
		for(ItemVenda item : itens) {
			incluirItem(item);
		}
		
		return this;
	}
	
	public VendaBuilder comItem(ItemVenda item) {
		return comItens(Arrays.asList(item));
	}
	
	private void incluirItem(ItemVenda item) {
		this.venda.adicionaItem(item);
	}
	
	public Venda construir() {
		return this.venda;
	}
}
