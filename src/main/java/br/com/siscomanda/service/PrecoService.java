package br.com.siscomanda.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Preco;
import br.com.siscomanda.model.Produto;
import br.com.siscomanda.model.Tamanho;
import br.com.siscomanda.repository.dao.PrecoDAO;

public class PrecoService implements Serializable {

	private static final long serialVersionUID = -7070491193386801731L;
	
	@Inject
	private PrecoDAO dao;
	
	public List<Preco> porProduto(Produto produto) throws SiscomandaException {
		List<Preco> precos = dao.porProduto(produto);
		
		if(precos == null || precos.isEmpty()) {
			throw new SiscomandaException("Não existe preços disponiveis para este produto.");
		}
		
		return precos;
	}
	
	public List<Preco> porTamanhoProdutos(List<Produto> produtos, Tamanho tamanho) throws SiscomandaException {
		List<Preco> precos = dao.porTamanhoProduto(produtos, tamanho);
		
		if(precos == null || precos.isEmpty()) {
			throw new SiscomandaException("Não existe preços disponiveis para este produto.");
		}
		
		return precos;
	}
	
	public Preco porValor(Double valor, Produto produto) throws SiscomandaException {
		return dao.porValor(valor, produto);
	}
}
