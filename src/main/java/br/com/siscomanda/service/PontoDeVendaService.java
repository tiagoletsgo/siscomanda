package br.com.siscomanda.service;

import java.io.Serializable;
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
		double quantidadeItens = new Double(1) / new Double(itens.size());
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
						item.setValor(preco.getPrecoVenda());
						item.setTotal(item.getValor() * quantidadeItens);
					}
				}
			}			
		}
				
		return itens;
	}
	
	public Double calcularValorTotal(List<ItemVenda> itens, ItemVenda item) {
		double total = itens.isEmpty() ? item.getValor() : 0;
		double totalComplemento = 0;
		double quantidadeItens = !itens.isEmpty() ? (new Double(1) / new Double(itens.size())) : 1;
		
		if(!itens.isEmpty()) {
			for(ItemVenda iten : itens) {
				if(!iten.getAdicionais().isEmpty()) {
					for(Adicional complemento : iten.getAdicionais()) {
						totalComplemento += complemento.getPrecoVenda();
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
		
		return (total * quantidadeItens) + totalComplemento;
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
			
			Map<ItemVenda, List<Adicional>> adicionais = new HashMap<ItemVenda, List<Adicional>>();
			Map<ItemVenda, String> observacoes = new HashMap<ItemVenda, String>();
			
			if(itens.isEmpty()) {
				itens.add(item);
			}
			
			// para manter dados que ja existam
			for(ItemVenda iten : itens) {
				produtos.add(iten.getProduto());
				adicionais.put(iten, iten.getAdicionais());
				observacoes.put(iten, iten.getObservacao());
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
						Double quantidade = new Double(1) / new Double(produtos.size());
						ItemVenda itemm = new ItemVenda(venda, produto, (preco.getPrecoVenda()), quantidade, "");
						itemm.setId(id);
						itemm.setTamanho(tamanho);
						itens.add(itemm);
					}
				}
			}
			
			// recupera os adicionais
			for(Map.Entry<ItemVenda, List<Adicional>> entry : adicionais.entrySet()) {
				for(ItemVenda iten : itens) {
					if(iten.getProduto().equals(entry.getKey().getProduto())) {
						iten.setAdicionais(entry.getValue());
					}
				}
			}
			
			// recupera as observaçoes
			for(Map.Entry<ItemVenda, String> entry : observacoes.entrySet()) {
				for(ItemVenda iten : itens) {
					if(iten.getProduto().equals(entry.getKey().getProduto())) {
						iten.setObservacao(entry.getValue());
					}
				}
			}
		}
		
		return itens;
	}
	
	@Deprecated
	public Map<String, Object> atualizaNomeDosProdutos(List<ItemVenda> itens, String descricaoOriginalProduto, String sigla) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		boolean ePersonalizado = itens.size() > 1 ? true : false;
		String descricaoAlteradaProduto = null;
		
		for(ItemVenda item : itens) {
			String descricaoProduto = item.getProduto().getDescricao().replaceAll("\\(.+?\\)", "");
			descricaoProduto = descricaoProduto + "(" + sigla + ")";

			Produto produto = item.getProduto();
			produto.setDescricao(descricaoProduto);
			item.setProduto(produto.clone(produto));
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
	
	public List<ItemVenda> incluirObservacao(List<ItemVenda> itens, ItemVenda itemSelecionado, String observacao) {
		if(!itens.isEmpty()) {
			for(ItemVenda item : itens) {
				if(item.equals(itemSelecionado)) {
					item.setObservacao(observacao);
					break;
				}
			}
		}
		
		return itens;
	}
	
	public String observacao(List<ItemVenda> itens, ItemVenda item) {
		String observacao = "";
		
		if(itens.isEmpty()) {
			observacao = item.getObservacao();
		}
		else {
			for(ItemVenda itemm : itens) {
				if(itemm.getProduto().equals(item.getProduto())) {
					observacao = itemm.getObservacao();
				}
			}
		}
		
		return observacao;
	}
}
