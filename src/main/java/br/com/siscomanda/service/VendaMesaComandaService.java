package br.com.siscomanda.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import br.com.siscomanda.exception.SiscomandaRuntimeException;
import br.com.siscomanda.model.DefinicaoGeral;
import br.com.siscomanda.model.ItemVenda;
import br.com.siscomanda.model.Produto;
import br.com.siscomanda.repository.dao.VendaMesaComandaDAO;

public class VendaMesaComandaService implements Serializable {

	private static final long serialVersionUID = -7365230528253341931L;
	
	@Inject
	private VendaMesaComandaDAO dao;
	
	@Inject
	private DefinicaoGeralService definicaoGeralService;
	
	public ItemVenda clonaItemVenda(ItemVenda itemVenda, Produto produto, Integer quantidade) {
		if(produto == null) {
			ItemVenda item = new ItemVenda();
			
			item = itemVenda;
			item.setQuantidade(quantidade);
			item.setSubtotal(item.getQuantidade() * item.getProduto().getPrecoVenda());
			return item;
		}
		return itemVenda;
	}
	
	public List<Integer> geraMesasComandas() {
		List<Integer> mesas = new ArrayList<>();
		int qtdMesasComandas = definicaoGeralService.carregaDefinicaoSistema().getQtdMesaComanda();
		for(int i = 0; i < qtdMesasComandas; i++) {
			mesas.add(i + 1);
		}
		return mesas;
	}
	
	public void adicionaItem(List<ItemVenda> itens, ItemVenda item) {
		boolean itemEncontrado = false;
		for(ItemVenda itemVenda : itens) {
			if(itemVenda.getProduto().equals(item.getProduto())) {
				itemVenda.setQuantidade(itemVenda.getQuantidade() + 1);
				itemVenda.setSubtotal(itemVenda.getQuantidade() * itemVenda.getProduto().getPrecoVenda());
				itemEncontrado = true;
				break;
			}
		}
		if(!itens.contains(item) && !itemEncontrado) {
			itens.add(item);
		}
	}
	
	public void removeItem(List<ItemVenda> itens, ItemVenda item, Produto produto) {
		List<ItemVenda> itensVendasTemp = itens;
		for(ItemVenda itemVenda : itensVendasTemp) {
			if(itemVenda.getProduto().equals(produto == null ? item.getProduto() : produto)) {
				if(item.getQuantidade() > itemVenda.getQuantidade()) {
					throw new SiscomandaRuntimeException("A quantidade informada não pode ser maior que a quantidade do pedido.");
				}
				if(itemVenda.getQuantidade() == new Integer(1)) {
					itens.remove(itemVenda);
					break;
				}
				itemVenda.setQuantidade(itemVenda.getQuantidade() - 1);
				itemVenda.setSubtotal(itemVenda.getQuantidade() * itemVenda.getProduto().getPrecoVenda());
				break;
			}
		}
	}
	
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
