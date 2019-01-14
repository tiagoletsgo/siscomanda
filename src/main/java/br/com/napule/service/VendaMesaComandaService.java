package br.com.napule.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.napule.model.ItemVenda;
import br.com.napule.model.Produto;
import br.com.napule.repository.dao.VendaMesaComandaDAO;

public class VendaMesaComandaService implements Serializable {

	private static final long serialVersionUID = -7365230528253341931L;
	
	@Inject
	private VendaMesaComandaDAO dao;
	
	public Long setIdTemporarioItem(List<ItemVenda> itens) {
		Long id = 1L;
		for(ItemVenda item : itens) {
			item.setId(item.getId() + 1);
		}
		return id;
	}
	
	public ItemVenda adicionaItemPedidoVenda(Produto produto) {
		ItemVenda item = new ItemVenda();
		item.setProduto(produto);
		item.setQuantidade(1);
		item.setSubtotal(produto.getPrecoVenda() * item.getQuantidade());
		return item;
	}
	
	public List<ItemVenda> ordenarItemMenorParaMaior(List<ItemVenda> itens) {
//		itens.sort((item1, item2) -> item1.getId().compareTo(item2.getId()));
		return itens;
	}
}
