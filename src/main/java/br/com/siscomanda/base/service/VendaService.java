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

public abstract class VendaService implements Serializable {

	private static final long serialVersionUID = -8273876283694756574L;
	
	@Inject
	private DefinicaoGeralService definicaoGeralService;
	
	private List<Produto> listTemp = new ArrayList<>();
	
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
	
	public Long setIdTemporarioItem(List<ItemVenda> itens) {
		Long id = 1L;
		for(ItemVenda item : itens) {
			id = item.getId() +1;
		}
		return id;
	}
	
	public void removeIdTemporario(List<ItemVenda> itens) {
		for(ItemVenda item : itens) {
			item.setId(null);
		}
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
			for(Adicional adicional : item.getAdicionais()) {
				subtotal += adicional.getPrecoVenda();
			}
			
			subtotal += item.getSubtotal();
		}
		return subtotal;
	}
	
	public Double calculaSubTotalItem(ItemVenda item) {
		Double subtotal = new Double(0);
		for(Adicional adicional : item.getAdicionais()) {
			subtotal += adicional.getPrecoVenda();
		}
		return subtotal + item.getPrecoVenda();
	}
	
	public Double calculaTotal(Venda venda) {
		return (venda.getSubtotal() + venda.getTaxaEntrega() + venda.getTaxaServico()) - venda.getDesconto();
	}
	
	public List<ItemVenda> ordenarItemMenorParaMaior(List<ItemVenda> itens) {
		Collections.sort(itens);
		return itens;
	}
			
	public void incluirItem(Venda venda, List<Adicional> adicionais, ItemVenda item, Produto produto, Double quantidade) throws SiscomandaException {
		
		if(quantidade.equals(new Double(0))) {
			throw new SiscomandaException(produto.getDescricao() + " necessário informar a quantidade antes de salvar. Por favor tente novamente.");
		}
		
		ItemVenda itemVenda = new ItemVenda();
		itemVenda.setId(setIdTemporarioItem(venda.getItens()));
		itemVenda.setProduto(produto);
		itemVenda.setQuantidade(quantidade);
		itemVenda.setPrecoVenda(produto.getPrecoVenda());
		itemVenda.setObservacao(item.getObservacao());
		itemVenda.setSubtotal(quantidade * produto.getPrecoVenda());
		itemVenda.setVenda(venda);
		
		List<Adicional> tempAdicionais = new ArrayList<>();
		if(adicionais != null) {
			for(Adicional adc : adicionais) {
				Adicional adicional = new Adicional();
				adicional.setId(adc.getId());
				adicional.setDescricao(adc.getDescricao());
				adicional.setPrecoVenda(adc.getPrecoVenda());
				adicional.setCategorias(adc.getCategorias());
				adicional.setControlaEstoque(adc.getControlaEstoque());
				adicional.setPrecoVenda(adc.getPrecoVenda());
				adicional.setQuantidade(new Double(1));
				
				tempAdicionais.add(adicional);
			}
		}
		
		itemVenda.setAdicionais(tempAdicionais);
		venda.getItens().add(itemVenda);
	}
		
	public ItemVenda clonar(Produto produto, List<Adicional> adicionais) {
		ItemVenda item = null;
		
		if(produto != null) {
			item = new ItemVenda();
			item.setProduto(produto);
			item.setAdicionais(adicionais);
			return item;
		}
		
		return item;
	}
	
	public List<ItemVenda> removeItem(List<ItemVenda> itens, ItemVenda item, Double quantidade) throws SiscomandaException {
		List<ItemVenda> itensVenda = new ArrayList<>();
		itensVenda.addAll(itens);
		
		for(ItemVenda itemVenda : itensVenda) {
			if(itemVenda.getProduto().equals(item.getProduto()) && itemVenda.getProduto().getCodigoEan().equals(item.getProduto().getCodigoEan())) {
				if(quantidade <= new Double(0)) {
					throw new SiscomandaException("Não são permitidos valores negativos, zerados ou vazios.");
				}
				if(quantidade > itemVenda.getQuantidade()) {
					throw new SiscomandaException("Produto com quantidade insuficiente para ser removido.");
				}
				if(itemVenda.getQuantidade().equals(new Double(1)) || itemVenda.getQuantidade().equals(quantidade)) {
					itens.remove(itemVenda);
					break;
				}
				
				itemVenda.setQuantidade(itemVenda.getQuantidade() - quantidade);
				itemVenda.setSubtotal(itemVenda.getQuantidade() * itemVenda.getProduto().getPrecoVenda());
				break;
			}
		}
		
		return itens;
	}
}
