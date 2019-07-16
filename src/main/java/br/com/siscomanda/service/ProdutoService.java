package br.com.siscomanda.service;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import br.com.siscomanda.config.jpa.Transactional;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Produto;
import br.com.siscomanda.repository.dao.ProdutoDAO;
import br.com.siscomanda.util.JSFUtil;
import br.com.siscomanda.util.StringUtil;

public class ProdutoService implements Serializable {


	private static final long serialVersionUID = -2167240798275553918L;
		
	@Inject
	private ProdutoDAO dao;
		
	public List<Produto> pesquisar(Produto produto) throws SiscomandaException {
		List<Produto> list = null;
		verificaCampoSelecionado(produto);
		list = dao.buscaPor(produto);	
		return list;
	}
	
	private void verificaCampoSelecionado(Produto produto) throws SiscomandaException {
		StringUtil.maisDeUmCampoPreenchido(Arrays.asList(
					produto.getDescricao(),
					produto.getCodigoEan()
				));
	}
	
	@Transactional
	public Produto salvar(Produto produto) throws SiscomandaException {
		validacao(produto);
		if(produto.isNovo()) {
			produto = dao.salvar(produto);
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro salvo com sucesso.");
			
			return produto;
		}
		
		JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro alterado com sucesso.");
		return dao.salvar(produto);
	}
	
	public Produto porCodigo(Produto produto) throws SiscomandaException {
		produto = dao.porCodigo(produto);
		return produto;
	}
	
	private void validacao(Produto produto) throws SiscomandaException {
		
		if(StringUtil.isEmpty(produto.getDescricao())) {
			throw new SiscomandaException("É necessário informar o nome do produto.!");
		}
		
		if(StringUtil.isEmpty(produto.getCodigoEan())) {
			throw new SiscomandaException("É necessário informar o código do produto.!");
		}
		
		if(produto.getEmbalagem() == null) {
			throw new SiscomandaException("É necessário informar qual a embalagem.!");
		}
		
		if(produto.getFornecedor() == null) {
			throw new SiscomandaException("É necessário informar o fornecedor.!");
		}
		
		if(produto.getCategoria() == null) {
			throw new SiscomandaException("É necessário informar a categoria.!");
		}
		
		if(produto.getSubCategoria() == null) {
			throw new SiscomandaException("É necessário informar a subcategoria.!");
		}
			
		if(produto.isNovo()) {
			if(dao.isExists(produto)) {
				throw new SiscomandaException("Esse produto já se encontra cadastro no sistema.!");
			}
		}		
	}
	
	public List<Produto> todos() {
		return dao.todos(Produto.class);
	}
	
	public List<Produto> todos(boolean permitePersonalizar) {
		return dao.todos(permitePersonalizar);
	}
	
	@Transactional
	public void remover(List<Produto> produtos) throws SiscomandaException {
		if(produtos == null || produtos.isEmpty()) {
			throw new SiscomandaException("Selecione pelo menos 1 registro para ser excluído.!");
		}
		
		try {
			for(Produto produto : produtos) {
				dao.remover(Produto.class, produto.getId());
			}
		}
		catch(SiscomandaException e) {
			throw new SiscomandaException(e.getMessage(), e);
		}
	}
}
