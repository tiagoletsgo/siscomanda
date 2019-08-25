package br.com.siscomanda.vo;

import java.io.Serializable;
import java.util.List;

import br.com.siscomanda.enumeration.EStatus;
import br.com.siscomanda.enumeration.ETipoVenda;
import br.com.siscomanda.model.Venda;

public class TotalizadorVO implements Serializable {
	
	private static final long serialVersionUID = 491825269402037308L;
	private double totalVendaDelivery;
	private double totalVendaBalcao;
	private double totalVendaConfig;
	private int totalPedido;
	private double totalVendaCancelada;
	
	public TotalizadorVO(List<Venda> vendas) {
		totalPedido = vendas.size();
		totalVendaBalcao = calcular(vendas, ETipoVenda.BALCAO);
		totalVendaConfig = calcular(vendas, ETipoVenda.COMANDA);
		totalVendaConfig += calcular(vendas, ETipoVenda.MESA);
		totalVendaDelivery = calcular(vendas, ETipoVenda.DELIVERY);
		totalVendaCancelada = calcular(vendas, null);
	}
	
	private double calcular(List<Venda> vendas, ETipoVenda tipoVenda) {
		double soma = 0D;
		for(Venda venda : vendas) {
			if(tipoVenda != null && venda.getTipoVenda().equals(tipoVenda)) {
				soma += venda.getTotal();
			}
			if(tipoVenda == null && venda.getStatus().equals(EStatus.CANCELADO)) {
				soma += venda.getTotal();
			}
		}
		
		return soma;
	}
	
	public double getTotalVenda() {
		return (this.totalVendaBalcao + this.totalVendaConfig + this.totalVendaDelivery) - this.totalVendaCancelada;
	}

	public double getTotalVendaDelivery() {
		return totalVendaDelivery;
	}

	public double getTotalVendaBalcao() {
		return totalVendaBalcao;
	}
	
	public double getTotalVendaConfig() {
		return totalVendaConfig;
	}

	public int getTotalPedido() {
		return totalPedido;
	}

	public double getTotalVendaCancelada() {
		return totalVendaCancelada;
	}
}
