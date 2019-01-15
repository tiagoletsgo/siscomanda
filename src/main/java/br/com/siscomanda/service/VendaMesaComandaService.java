package br.com.siscomanda.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import br.com.siscomanda.model.ItemVenda;
import br.com.siscomanda.model.Produto;
import br.com.siscomanda.repository.dao.VendaMesaComandaDAO;

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
		Collections.sort(itens);
		return itens;
	}
	
	public static void main(String[] args) {
		List<Integer> mesas = new ArrayList<>();
		mesas.add(5);
		mesas.add(1);
		mesas.add(4);
		mesas.add(2);
		mesas.add(3);
		Collections.sort(mesas);
		for(Integer i : mesas) {
			System.out.println(i);
		}
	}
}
