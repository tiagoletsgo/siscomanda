package br.com.siscomanda.base.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Adicional;
import br.com.siscomanda.model.ItemVenda;
import br.com.siscomanda.model.Produto;
import br.com.siscomanda.model.Venda;
import br.com.siscomanda.service.DefinicaoGeralService;
import br.com.siscomanda.util.JSFUtil;
import br.com.siscomanda.util.StringUtil;

public abstract class VendaService implements Serializable {

	private static final long serialVersionUID = -8273876283694756574L;
	
	@Inject
	private DefinicaoGeralService definicaoGeralService;
	
	private List<Produto> listTemp = new ArrayList<>();
	
	private static String ADICIONAL = "adicional";
	
	public Double getTaxaServico() {
		Double valor = definicaoGeralService.carregaDefinicaoSistema().getTaxaServico();
		return (valor / 100);
	}
	
	public List<Produto> validaQuantidadePermitida(List<Produto> produtos) {
		
		Produto produtoCheck = new Produto();
		
		if(produtos.size() < listTemp.size()) {
			listTemp.clear();
			listTemp.addAll(produtos);
		}
		
		if(produtos.size() > listTemp.size()) {
			for(Produto produto : produtos) {
				if(!listTemp.contains(produto)) {
					produtoCheck = produto;
					listTemp.add(produtoCheck);
					break;
				}
			}
		}
		
		if(produtos.size() > definicaoGeralService.carregaDefinicaoSistema().getPermiteQuantoSabores()) {
			listTemp.remove(produtoCheck);
			produtos.remove(produtoCheck);
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "A quantidade de sabores selecionado excede o limite configurado.");
		}
		
		return produtos;
	}
	
	public List<Integer> geraMesasComandas() {
		List<Integer> mesas = new ArrayList<>();
		int qtdMesasComandas = definicaoGeralService.carregaDefinicaoSistema().getQtdMesaComanda();
		for(int i = 0; i < qtdMesasComandas; i++) {
			mesas.add(i + 1);
		}
		return mesas;
	}
	
	public Double calculaSubtotal(List<ItemVenda> itens) {
		Double subtotal = new Double(0);
		for(ItemVenda item : itens) {
			subtotal += item.getSubtotal();
		}
		return subtotal;
	}
	
	public Double calculaTotal(Venda venda) {
		return (venda.getSubtotal() + venda.getTaxaEntrega() + venda.getTaxaServico()) - venda.getDesconto();
	}
	
	public Long setIdTemporarioItem(List<ItemVenda> itens) {
		Long id = 1L;
		for(ItemVenda item : itens) {
			item.setId(item.getId() + 1);
		}
		return id;
	}
	
	public List<ItemVenda> ordenarItemMenorParaMaior(List<ItemVenda> itens) {
		Collections.sort(itens);
		return itens;
	}
	
	public ItemVenda clonaItemVenda(ItemVenda itemVenda, Produto produto, Double quantidade) {
		ItemVenda item = null;
		if(produto == null) {
			item = new ItemVenda();
			item.setQuantidade(quantidade == null ? new Double(0) : quantidade);
			item.setProduto(itemVenda.getProduto());
			item.setSubtotal(item.getQuantidade() * item.getProduto().getPrecoVenda());
			return item;
		}
		if(itemVenda == null) {
			item = new ItemVenda();
			item.setProduto(produto);
			item.setQuantidade(new Double(1));
			return item;
		}
		
		return itemVenda;
	}
	
//	public void adicionaItem(List<ItemVenda> itens, ItemVenda item) {
//		boolean itemEncontrado = false;
//		for(ItemVenda itemVenda : itens) {
//			if(itemVenda.getProduto().equals(item.getProduto())) {
//				itemVenda.setQuantidade(itemVenda.getQuantidade() + 1);
//				itemVenda.setPrecoVenda(item.getProduto().getPrecoVenda());
//				itemVenda.setSubtotal(itemVenda.getQuantidade() * itemVenda.getProduto().getPrecoVenda());
//				itemEncontrado = true;
//				break;
//			}
//		}
//		if(!itens.contains(item) && !itemEncontrado) {
//			item.setPrecoVenda(item.getProduto().getPrecoVenda());
//			itens.add(item);
//		}
//	}
	
	public void incluirItem(List<ItemVenda> itens, List<Adicional> adicionais, ItemVenda item, Produto produto, Double quantidade) {
		ItemVenda itemVenda = new ItemVenda();
		itemVenda.setId(setIdTemporarioItem(itens));
		itemVenda.setProduto(produto);
		itemVenda.setQuantidade(quantidade);
		itemVenda.setPrecoVenda(produto.getPrecoVenda());
		itemVenda.setObservacao(item.getObservacao());
		itemVenda.setAdicionais(adicionais);
		itemVenda.setSubtotal(quantidade * produto.getPrecoVenda());
		itens.add(itemVenda);
	}
	
	public void incluirAdicionais(List<ItemVenda> itens, List<Adicional> adicionais) {
		for(Adicional adicional : adicionais) {
			Produto produto = new Produto();
			produto.setId(adicional.getId());
			produto.setCodigoEan("+");
			produto.setDescricao(adicional.getDescricao().concat(" (").concat(ADICIONAL.toUpperCase()).concat(")"));
			
			ItemVenda item = new ItemVenda();
			item.setId(setIdTemporarioItem(itens));
			item.setPrecoVenda(adicional.getPrecoVenda());
			item.setProduto(produto);
			item.setQuantidade(new Double(1));
			item.setSubtotal(item.getQuantidade() * item.getPrecoVenda());
			itens.add(item);
		}
	}
	
	public void removeItem(List<ItemVenda> itens, ItemVenda item, Produto produto) throws SiscomandaException {
		item = clonaItemVenda(item, produto, produto == null ? item.getQuantidade() : new Double(1));
		List<ItemVenda> itensVendasTemp = itens;
		
		for(ItemVenda itemVenda : itensVendasTemp) {
			if(itemVenda.getProduto().equals(produto == null ? item.getProduto() : produto)) {
				if(item.getQuantidade() <= new Double(0)) {
					throw new SiscomandaException("Não são permitidos valores negativos, zerados ou vazios.");
				}
				if(item.getQuantidade() > itemVenda.getQuantidade()) {
					throw new SiscomandaException("Somente " + StringUtil.converterDouble(itemVenda.getQuantidade()) + " iten(s) pode(m) ser removido(s)!");
				}
				if(itemVenda.getQuantidade().equals(new Double(1)) || itemVenda.getQuantidade().equals(item.getQuantidade())) {
					itens.remove(itemVenda);
					break;
				}
				
				itemVenda.setQuantidade(itemVenda.getQuantidade() - (produto == null ? item.getQuantidade() : 1));
				itemVenda.setSubtotal(itemVenda.getQuantidade() * itemVenda.getProduto().getPrecoVenda());
				break;
			}
		}
	}
	
	public ItemVenda adicionaItemPedidoVenda(Produto produto) {
		ItemVenda item = new ItemVenda();
		item.setProduto(produto);
		item.setQuantidade(new Double(1));
		item.setSubtotal(produto.getPrecoVenda() * item.getQuantidade());
		return item;
	}
}
