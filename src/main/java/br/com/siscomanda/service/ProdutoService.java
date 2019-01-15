package br.com.siscomanda.service;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import br.com.siscomanda.config.jpa.Transactional;
import br.com.siscomanda.exception.NapuleException;
import br.com.siscomanda.model.Produto;
import br.com.siscomanda.repository.dao.ProdutoDAO;
import br.com.siscomanda.util.JSFUtil;
import br.com.siscomanda.util.StringUtil;

public class ProdutoService implements Serializable {


	private static final long serialVersionUID = -2167240798275553918L;
		
	@Inject
	private ProdutoDAO dao;
		
	public List<Produto> pesquisar(Produto produto) throws NapuleException {
		List<Produto> list = null;
		verificaCampoSelecionado(produto);
		list = dao.buscaPor(produto);	
		return list;
	}
	
	private void verificaCampoSelecionado(Produto produto) throws NapuleException {
		StringUtil.maisDeUmCampoPreenchido(Arrays.asList(
					produto.getDescricao(),
					produto.getCodigoEan()
				));
	}
	
	@Transactional
	public Produto salvar(Produto produto) throws NapuleException {
		produto = validacao(produto);
		if(produto.isNovo()) {
			produto = dao.salvar(produto);
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro salvo com sucesso.");
			
			return produto;
		}
		
		JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro alterado com sucesso.");
		return dao.salvar(produto);
	}
	
	public Produto porCodigo(Produto produto) throws NapuleException {
		return dao.porCodigo(produto);
	}
	
	private Produto validacao(Produto produto) throws NapuleException {
		
		if(StringUtil.isEmpty(produto.getDescricao())) {
			throw new NapuleException("É necessário informar o nome do produto.!");
		}
		
		if(StringUtil.isEmpty(produto.getCodigoEan())) {
			throw new NapuleException("É necessário informar o código do produto.!");
		}
		
		if(produto.getEmbalagem() == null) {
			throw new NapuleException("É necessário informar qual a embalagem.!");
		}
		
		if(produto.getFornecedor() == null) {
			throw new NapuleException("É necessário informar o fornecedor.!");
		}
		
		if(produto.getCategoria() == null) {
			throw new NapuleException("É necessário informar a categoria.!");
		}
		
		if(produto.getSubCategoria() == null) {
			throw new NapuleException("É necessário informar a subcategoria.!");
		}
		
		if(produto.getControlaEstoque() == null) {
			throw new NapuleException("Informe se este produto controla ou não estoque.!");
		}
		
		if(produto.getPrecoVenda().equals(new Double("0"))) {
			throw new NapuleException("Informe o preço de venda.!");
		}
		
		if(produto.isNovo()) {
			if(dao.isExists(produto)) {
				throw new NapuleException("Esse produto já se encontra cadastro no sistema.!");
			}
		}
		
		return produto;
	}
	
	public List<Produto> todos() {
		return dao.todos(Produto.class);
	}
	
	@Transactional
	public void remover(List<Produto> produtos) throws NapuleException {
		if(produtos == null || produtos.isEmpty()) {
			throw new NapuleException("Selecione pelo menos 1 registro para ser excluído.!");
		}
		
		try {
			for(Produto produto : produtos) {
				dao.remover(Produto.class, produto.getId());
			}
		}
		catch(NapuleException e) {
			throw new NapuleException(e.getMessage(), e);
		}
	}
}
