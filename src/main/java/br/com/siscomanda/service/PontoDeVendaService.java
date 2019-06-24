package br.com.siscomanda.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import br.com.siscomanda.exception.SiscomandaException;
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
	
	public ItemVenda paraItemVenda(Venda venda, Produto produto) {
		double valor = 0;
		double quantidade = 1;
		return new ItemVenda(venda, produto, valor, quantidade, "");
	}
	
//	public ItemVenda alterarValorTotalPara(ItemVenda item) {
//		item.setValor(item.getValor());
//		item.setTotal(item.getValor() * item.getQuantidade());
//		return item;
//	}
	
//	public List<ItemVenda> paraItemVenda(Venda venda, List<Produto> produtos, Tamanho tamanho) {
//		List<ItemVenda> itens = new ArrayList<ItemVenda>();
//		if(!produtos.isEmpty()) {
//			List<Preco> precos = precoDAO.porTamanhoProduto(produtos, tamanho);
//			
//			int i = 0;
//			for(Preco preco : precos) {
//				itens.add(paraItemVenda(venda, preco.getProduto()));
//				itens.get(i).setValor(preco.getPrecoVenda());
//				itens.get(i).setId(new BigDecimal(i + 1).longValue());
//				i++;
//			}
//		}
//		
//		return itens;
//	}
	
	public List<Adicional> desmarcarListaDeComplementos(List<Adicional> complementos, List<ItemVenda> itens, ItemVenda item) {
		for(Adicional complemento : complementos) {
			complemento.setSelecionado(false);
		}
		
		if(itens.isEmpty()) {
			for(Adicional complemento : complementos) {
				if(item.getAdicionais().contains(complemento)) {
					complemento.setSelecionado(true);
				}
			}
		}
		
		return complementos;
	}
	
	public List<ItemVenda> atualizaListaItemMeioAmeio(List<ItemVenda> itens, Tamanho tamanho) throws SiscomandaException {
		List<Produto> produtos = new ArrayList<Produto>();
		
		if(!itens.isEmpty()) {
			for(ItemVenda item : itens) {
				produtos.add(item.getProduto());
			}
			
			List<Preco> precos = precoDAO.porTamanhoProduto(produtos, tamanho);
			
			if(precos.isEmpty()) {
				throw new SiscomandaException("Não foi possivel atualizar a lista de itens meio a meio.");
			}
			
			for(Preco preco : precos) {
				for(ItemVenda item : itens) {
					if(item.getProduto().equals(preco.getProduto())) {							
						item.setValor((preco.getPrecoVenda() / produtos.size()));
						item.setTotal(item.getValor() * item.getQuantidade());
					}
				}
			}			
		}
				
		return itens;
	}
	
	public Double calcularValorTotal(List<ItemVenda> itens, ItemVenda item) {
		double total = itens.isEmpty() ? item.getValor() : 0;
		
		if(!itens.isEmpty()) {
			for(ItemVenda iten : itens) {
				if(!iten.getAdicionais().isEmpty()) {
					for(Adicional complemento : iten.getAdicionais()) {
						total += complemento.getPrecoVenda();
					}
				}
				total += iten.getValor();
			}
		}
		else 
			if(itens.isEmpty()) {
				for(Adicional complemento : item.getAdicionais()) {
					total += complemento.getPrecoVenda();
				}
		}
		
		return total;
	}
	
	public List<ItemVenda> removerItem(List<ItemVenda> itens, ItemVenda item, Produto produto) throws SiscomandaException {
		if(!itens.contains(item)) {
			throw new SiscomandaException("Este item não foi encontrado na lista.");
		}
		
		if(itens.size() == 2 || item.getProduto().equals(produto)) {
			itens.clear();
		}
		else {			
			itens.remove(item);
		}
		
		return itens;
	}
	
	public List<Adicional> atualizaListaDeComplementos(ItemVenda itemSelecionado, ItemVenda itemPrincipal, List<Adicional> complementos) {
		if(itemSelecionado == null) {
			itemSelecionado = itemPrincipal;
		}
		
		if(!itemSelecionado.getAdicionais().isEmpty()) {
			for(Adicional complemento : complementos) {
				if(itemSelecionado.getAdicionais().contains(complemento)) {
					complemento.setSelecionado(true);
				}
				else {
					complemento.setSelecionado(false);
				}
			}
		}
		else {
			for(Adicional complement : complementos) {
				complement.setSelecionado(false);
			}
		}
		
		return complementos;
	}
	
	public List<ItemVenda> adicionaComplemento(ItemVenda itemSelecionado, ItemVenda itemPrincipal, Adicional complemento, List<Adicional> complementos, List<ItemVenda> itens) throws SiscomandaException {
		if(itemSelecionado == null && !itens.isEmpty()) {
			for(Adicional adicional : complementos) {
				adicional.setSelecionado(false);
			}
			throw new SiscomandaException("Antes de incluir um complemento por gentileza, selecione um item da lista de itens meio a meio.");
		}
		if(itemSelecionado == null && complemento.isSelecionado()) {
			itemSelecionado = itemPrincipal;
			itens.add(itemSelecionado);
		}
				
		if(complemento.isSelecionado()) {
			for(Adicional adicional: complementos) {
				if(adicional.isSelecionado()) {
					if(!itemSelecionado.getAdicionais().contains(adicional)) {					
						itemSelecionado.getAdicionais().add(adicional);
						adicional.setSelecionado(false);
					}
				}
			}
			
			for(ItemVenda iten : itens) {
				if(iten.equals(itemSelecionado)) {
					iten = itemSelecionado;
				}
			}
		}
		else if(itemSelecionado != null) {
			itemSelecionado.getAdicionais().remove(complemento);
		}
		else {
			itemPrincipal.getAdicionais().remove(complemento);
			itens.add(itemPrincipal);
		}
		
		return itens;
	}
	
	public List<Produto> corrigeNomeDaListaDeProdutos(List<Produto> produtos) {
		for(Produto produto : produtos) {
			String descricao = produto.getDescricao().replaceAll("\\(.+?\\)", "");
			produto.setDescricao(descricao);
		}
		return produtos;
	}
	
	public List<ItemVenda> personalizar(List<Produto> produtos, List<ItemVenda> itens, Tamanho tamanho, ItemVenda item, Venda venda) throws SiscomandaException {
		if(produtos != null) {
			for(Produto produto : produtos) {
				for(ItemVenda iten : itens) {
					if(iten.getProduto().equals(produto)) {
						throw new SiscomandaException("Este(s) produto (s) já se encontra(m) incluido(s).");
					}
				}
			}
			
			Map<Produto, List<Adicional>> map = new HashMap<Produto, List<Adicional>>();
			
			if(itens.isEmpty()) {
				itens.add(item);
			}
			
			for(ItemVenda iten : itens) {
				produtos.add(iten.getProduto());
				map.put(iten.getProduto(), iten.getAdicionais());
			}
			
			itens.clear();
			
			if(!produtos.contains(item.getProduto())) {					
				produtos.add(item.getProduto());
			}
			
			List<Preco> precos = precoDAO.porTamanhoProduto(produtos, tamanho);
			
			long id = 1;
			for(Preco preco : precos) {
				for(Produto produto : produtos) {
					if(produto.equals(preco.getProduto())) {
						id++;						
						ItemVenda itemm = new ItemVenda(venda, produto, (preco.getPrecoVenda() / produtos.size()), 1.0, "");
						itemm.setId(id);
						
						itens.add(itemm);
					}
				}
			}
			
			for(Map.Entry<Produto, List<Adicional>> entry : map.entrySet()) {
				for(ItemVenda iten : itens) {
					if(iten.getProduto().equals(entry.getKey())) {
						iten.setAdicionais(entry.getValue());
					}
				}
			}
		}
		
		return itens;
	}
	
	public Map<String, Object> atualizaNomeDosProdutos(List<ItemVenda> itens, String descricaoOriginalProduto, String sigla) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		boolean ePersonalizado = itens.size() > 1 ? true : false;
		String descricaoAlteradaProduto = null;
		
		for(ItemVenda item : itens) {
			String descricaoProduto = item.getProduto().getDescricao().replaceAll("\\(.+?\\)", "");
			descricaoProduto = descricaoProduto + "(" + sigla + ")";
			item.getProduto().setDescricao(descricaoProduto);
		}
		
		if(ePersonalizado) {
			descricaoAlteradaProduto = "PIZZA PERSONALIZADA (" + sigla + ")";
		}
		else {
			descricaoAlteradaProduto = descricaoOriginalProduto + "(" + sigla + ")";
		}
		
		map.put("descricaoProduto", descricaoAlteradaProduto);
		map.put("itens", itens);		
		
		return map;
	}
	
	public List<ItemVenda> listaItemMeioAmeioTiverUmRegistroRemovaTudo(List<ItemVenda> itens) {
		if(itens.size() == 1) {
			itens.clear();
		}
		return itens;
	}
}
