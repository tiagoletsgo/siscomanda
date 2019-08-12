package br.com.siscomanda.builder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import br.com.siscomanda.enumeration.EStatus;
import br.com.siscomanda.enumeration.ETipoVenda;
import br.com.siscomanda.model.Adicional;
import br.com.siscomanda.model.ItemVenda;
import br.com.siscomanda.model.Usuario;
import br.com.siscomanda.model.Venda;

public class VendaBuilder implements Serializable {
	
	private static final long serialVersionUID = 2294314144962576823L;
	private Long numeroVenda;
	private ETipoVenda tipoVenda;
	private EStatus status;
	private Usuario operador;
	private Date dataHora;
	private Double subtotal;
	private Double taxaServico;
	private Double taxaEntrega;
	private Double desconto;
	private Double valorTotal;
	private Integer controle;
	private Double valorPago;
	private List<ItemVenda> itens = new ArrayList<ItemVenda>();
	
	public VendaBuilder() {
		this.subtotal = new Double(0);
		this.taxaServico = new Double(0);
		this.taxaEntrega = new Double(0);
		this.desconto = new Double(0);
		this.valorTotal = new Double(0);
		this.controle = new Integer(0);
		this.valorPago = new Double(0);
		this.status = EStatus.EM_ABERTO;
		this.dataHora = new Date();
	}
	
	public VendaBuilder comNumeroVenda(Long numeroVenda) {
		this.numeroVenda = numeroVenda;
		return this;
	}
	
	public VendaBuilder comControle(Integer numeroControle) {
		this.controle = numeroControle;
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
	
	public VendaBuilder comOperador(Usuario operador) {
		this.operador = operador;
		return this;
	}
	
	public VendaBuilder comDataHora(Date dataHora) {
		this.dataHora = dataHora;
		return this;
	}
	
	public VendaBuilder comTaxaServico(Double taxaServico) {
		if(taxaServico == null) {
			taxaServico = new Double(0);
		}
		
		this.taxaServico = (taxaServico * subtotal) / 100;
		this.valorTotal = (this.taxaServico + subtotal);
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
	
	public VendaBuilder comItens(List<ItemVenda> itens) {
		long id = 0;
		for(ItemVenda item : itens) {
			
			item.setId(item.getId() == null ? id : item.getId());
			id++;
			
			incluirItem(item);
			somaItensComplementar(item.getAdicionais());
		}
		
		return this;
	}
	
	public VendaBuilder comItemPosicionado(Integer index, ItemVenda item) {
		calculaValorTotalDeItens(item);
		somaItensComplementar(item.getAdicionais());
		itens.add(index, item);
		return this;
	}
	
	public VendaBuilder comItem(ItemVenda item) {
		return comItens(Arrays.asList(item));
	}
	
	public VendaBuilder removerItemPorIndex(int index, ItemVenda item) { 
		item.setId(item.getId() == null ? index : item.getId());
		return removerItem(item);
//		calculaValorTotalDeItemRemovido(item);
//		itens.remove(index);
//		return this;
	}
	
	public VendaBuilder removerItem(ItemVenda item) {
		calculaValorTotalDeItemRemovido(item);
		itens.remove(item);
		return this;
	}
		
	private void incluirItem(ItemVenda item) {
		calculaValorTotalDeItens(item);
		itens.add(item);
	}
	
	private void somaItensComplementar(List<Adicional> complementos) {
		if(!complementos.isEmpty()) {
			for(Adicional complemento : complementos) {		
				this.subtotal += complemento.getPrecoVenda();
				this.valorTotal = subtotal;
			}
		}
	}
	
	private void calculaValorTotalDeItens(ItemVenda item) {
		this.subtotal += item.getValor() * item.getQuantidade(); 
		this.valorTotal += new Double(((item.getValor() * item.getQuantidade()) + taxaEntrega) - desconto);
	}
	
	private void calculaValorTotalDeItemRemovido(ItemVenda item) {
		this.subtotal -= item.getValor() * item.getQuantidade(); 
		this.valorTotal -= new Double(((item.getValor() * item.getQuantidade()) + taxaEntrega) - desconto);
		
		for(Adicional adicional : item.getAdicionais()) {
			this.subtotal -= adicional.getPrecoVenda();
			this.valorTotal = subtotal;
		}
	}
	
	private Venda insereIdSeNaoForNovaVenda(Venda venda) {
		Long id = this.numeroVenda;
		
		if(Objects.nonNull(id)) {
			venda.setId(id);
		}
		
		return venda;
	}
	
	public Venda construir() {
		Venda venda = new Venda(tipoVenda, status, operador, dataHora, subtotal, taxaServico, taxaEntrega, desconto, valorTotal, controle); 
		venda.setValorPago(valorPago);
		
		venda = insereIdSeNaoForNovaVenda(venda);
		Collections.sort(itens);
		
		for(ItemVenda item : itens) {
			venda.adicionaItem(item);
		}
		
		return venda;
	}
}
