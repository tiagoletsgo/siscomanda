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
import br.com.siscomanda.model.ItemVendaOLD;
import br.com.siscomanda.model.Produto;
import br.com.siscomanda.model.VendaOLD;
import br.com.siscomanda.service.ConfiguracaoGeralService;
import br.com.siscomanda.util.JSFUtil;

public abstract class VendaService implements Serializable {

	private static final long serialVersionUID = -8273876283694756574L;
	
	@Inject
	private ConfiguracaoGeralService configuracaoGeralService;
	
	private List<Produto> listTemp = new ArrayList<>();
		
	public Double getTaxaServico() {
		Double valor = configuracaoGeralService.definicaoSistema().getTaxaServico();
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
		
		if(produtos.size() > configuracaoGeralService.definicaoSistema().getPermiteQuantoSabores()) {
			listTemp.remove(produtoCheck);
			produtos.remove(produtoCheck);
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "A quantidade de sabores selecionado excede o limite configurado.");
		}
		
		return produtos;
	}
	
	public void validaQuantidadeTotalItens(List<ItemVendaOLD> itens) throws SiscomandaException {
		Double total = new Double(0);
		for(ItemVendaOLD item : itens) {
			total += item.getQuantidade();
		}
		
		String[] valor = total.toString().replace(".", ",").split(",");
		if(new Integer(valor[1]) > new Integer(0)) {
			throw new SiscomandaException("Quantidade total de itens não pode ser francionado.");
		}
	}
	
	public Long setIdTemporarioItem(List<ItemVendaOLD> itens) {
		Long id = 1L;
		for(ItemVendaOLD item : itens) {
			id = item.getId() +1;
		}
		return id;
	}
	
	public void removeIdTemporario(List<ItemVendaOLD> itens) {
		for(ItemVendaOLD item : itens) {
			item.setId(null);
		}
	}
	
	public Double calculaSubtotal(List<ItemVendaOLD> itens) {
		Double subtotal = new Double(0);
		for(ItemVendaOLD item : itens) {
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
		return subtotal + (item.getValor() * item.getQuantidade());
	}
	
	public Double calculaTotal(VendaOLD venda) {
		return (venda.getSubtotal() + venda.getTaxaEntrega() + venda.getTaxaServico()) - venda.getDesconto();
	}
	
	public List<ItemVendaOLD> ordenarItemMenorParaMaior(List<ItemVendaOLD> itens) {
		Collections.sort(itens);
		return itens;
	}
			
	public void incluirItem(VendaOLD venda, List<Adicional> adicionais, ItemVendaOLD item, Produto produto, Double quantidade) throws SiscomandaException {
		
		ItemVendaOLD itemVenda = null;

		if(quantidade.equals(new Double(0))) {
			throw new SiscomandaException(produto.getDescricao() + " necessário informar a quantidade antes de salvar. Por favor tente novamente.");
		}
		
		if(item.isNovo()) {
			itemVenda = new ItemVendaOLD();
			itemVenda.setId(setIdTemporarioItem(venda.getItens()));
			itemVenda.setProduto(produto);
			itemVenda.setQuantidade(quantidade);
//			itemVenda.setPrecoVenda(produto.getPrecoVenda());
			itemVenda.setObservacao(item.getObservacao());
//			itemVenda.setSubtotal(quantidade * produto.getPrecoVenda());
			itemVenda.setVenda(venda);
			
			itemVenda.setAdicionais(incluirAdicional(itemVenda, adicionais));
			venda.getItens().add(itemVenda);
		}
		else if(!item.isNovo()) {
			itemVenda = item;
			itemVenda.setAdicionais(incluirAdicional(itemVenda, adicionais));
			return;
		}
	}
	
	private List<Adicional> incluirAdicional(ItemVendaOLD item, List<Adicional> adicionais) {
		List<Adicional> cloneAdicionais = new ArrayList<>();
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
				cloneAdicionais.add(adicional);
			}
		}
		
		return cloneAdicionais;
	}
		
	public ItemVendaOLD clonar(Produto produto, List<Adicional> adicionais) {
		ItemVendaOLD item = null;
		
		if(produto != null) {
			item = new ItemVendaOLD();
			item.setProduto(produto);
			item.setAdicionais(adicionais);
			return item;
		}
		
		return item;
	}
	
	public List<ItemVendaOLD> removeItem(List<ItemVendaOLD> itens, ItemVendaOLD item, Double quantidade) throws SiscomandaException {
		List<ItemVendaOLD> itensVenda = new ArrayList<>();
		itensVenda.addAll(itens);
		
		for(ItemVendaOLD itemVenda : itensVenda) {
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
//				itemVenda.setSubtotal(itemVenda.getQuantidade() * itemVenda.getProduto().getPrecoVenda());
				break;
			}
		}
		
		return itens;
	}
}
