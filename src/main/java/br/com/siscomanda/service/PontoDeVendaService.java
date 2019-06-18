package br.com.siscomanda.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import br.com.siscomanda.model.Adicional;
import br.com.siscomanda.model.ItemVenda;
import br.com.siscomanda.model.Preco;
import br.com.siscomanda.model.Produto;
import br.com.siscomanda.model.Tamanho;
import br.com.siscomanda.model.Venda;
import br.com.siscomanda.repository.dao.PrecoDAO;

public class PontoDeVendaService implements Serializable {

	private static final long serialVersionUID = -8887278588654970978L;
	
	@Inject
	private PrecoDAO precoDAO;
	
	
	public ItemVenda item(Venda venda, Produto produto, Double valor, Double quantidade) {
		return new ItemVenda(venda, produto, valor, quantidade, "");
	}
	
	public ItemVenda item(Long id, Venda venda, Produto produto, Double valor, Double quantidade, List<Adicional> complementos) {
		return new ItemVenda(id, venda, produto, valor, quantidade, "", new ArrayList<Adicional>());
	}
	
	public List<ItemVenda> personalizar(List<Preco> precos, List<ItemVenda> itens, Double quantidade, Venda venda, List<Adicional> complementos) {
		
		long id = itens == null || itens.isEmpty() ? 1L : itens.get(itens.size() - 1).getId();
		Map<Produto, ItemVenda> items = new HashMap<Produto, ItemVenda>();
		
		if(!itens.isEmpty()) {
			for(ItemVenda item : itens) {
				items.put(item.getProduto(), item);
			}
		}
		
		for(Preco preco : precos) {
			id++;
			
			ItemVenda item = item(id, venda, preco.getProduto(), preco.getPrecoVenda(), quantidade, new ArrayList<Adicional>());
			items.put(preco.getProduto(), item);
		}
		
		itens = new ArrayList<ItemVenda>();
		for(Map.Entry<Produto, ItemVenda> entry : items.entrySet()) {
			itens.add(entry.getValue());
		}
		
		return itens;
	}
	
	public String atualizaNomeProduto(List<ItemVenda> itens, String descricaoAtual, String sigla) {
		// Regex para remover valores parenteses e os valores que 
		// contiver entre os parenteses.
		descricaoAtual = descricaoAtual.replaceAll("\\(.+?\\)", "");
		
		if(itens.size() > 1) {
			descricaoAtual = "PIZZA PERSONALIZADA (" + sigla.toUpperCase() + ")";
		}
		else {
			descricaoAtual = descricaoAtual + "(" + sigla + ")";
		}
		
		return descricaoAtual;
	}
	
	public List<ItemVenda> atualizaListaItemMeioAmeio(List<ItemVenda> itens, Venda venda, Tamanho tamanho) {
		List<Produto> produtos = new ArrayList<Produto>();
		List<ItemVenda> itensAtualizados = new ArrayList<ItemVenda>();
		
		if(!itens.isEmpty()) {
			for(ItemVenda item : itens) {
				produtos.add(item.getProduto());
			}
		}
		
		if(!produtos.isEmpty()) {
			List<Preco> precos = precoDAO.porTamanhoProduto(produtos, tamanho);
			
			long id = 0;
			for(ItemVenda item : itens) {
				for(Preco preco : precos) {
					if(item.getProduto().equals(preco.getProduto())) {
						id++;
						
						ItemVenda novoItem = item(id, venda, preco.getProduto(), preco.getPrecoVenda(), item.getQuantidade(), item.getAdicionais());
						itensAtualizados.add(novoItem);
					}
				}
			}
			
		}
		
		if(!itensAtualizados.isEmpty()) {			
			itens = itensAtualizados;
		}
		
		produtos = null;
		itensAtualizados = null;
		return itens;
	}
	
	public Double atualizaValorTotalItemPersonalizado(List<ItemVenda> itens, Double valorTotal, Double quantidade) {
		Double total = new Double("0");
		
		for(ItemVenda item : itens) {
			if(item.getAdicionais() != null) {				
				for(Adicional adicional : item.getAdicionais()) {
					if(adicional.isSelecionado()) {						
						valorTotal += adicional.getPrecoVenda();
					}
				}
			}
		}
		
		if(itens.isEmpty() || itens.size() == 1) {
			total += (valorTotal * quantidade);
		}
		else if(!itens.isEmpty()) {
			for(ItemVenda item : itens) {
				total += item.getValor() * quantidade;
			}
		}
		total = (valorTotal - total) + total;
		return total;
	}
	
	public List<ItemVenda> adicionaComplementos(List<ItemVenda> itens, ItemVenda item, Adicional complemento) {
		if(complemento.isSelecionado()) {
			item.getAdicionais().add(complemento);
		}
		else {
			item.getAdicionais().remove(complemento);
		}
		
		List<ItemVenda> items = new ArrayList<ItemVenda>();
		
		for(ItemVenda iten : itens) {
			if(iten.equals(item)) {
				iten = item;
			}
			items.add(iten);
		}
		
		return items;
	}
	
	public List<Adicional> atualizaComplementos(List<Adicional> complementos, ItemVenda item) {
		for(Adicional adicional : complementos) {
			if(item.getAdicionais().contains(adicional)) {
				adicional.setSelecionado(true);
			}
			else {
				adicional.setSelecionado(false);
			}
		}
		
		return complementos;
	}
}
