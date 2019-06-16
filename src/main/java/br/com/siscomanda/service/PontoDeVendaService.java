package br.com.siscomanda.service;

import java.io.Serializable;

import br.com.siscomanda.model.ItemVenda;
import br.com.siscomanda.model.Produto;
import br.com.siscomanda.model.Venda;

public class PontoDeVendaService implements Serializable {

	private static final long serialVersionUID = -8887278588654970978L;
	
	public ItemVenda item(Venda venda, Produto produto, Double valor, Double quantidade) {
		return new ItemVenda(venda, produto, valor, quantidade, "");
	}
}
