package br.com.siscomanda.service;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import br.com.siscomanda.config.jpa.Transactional;
import br.com.siscomanda.exception.NapuleException;
import br.com.siscomanda.model.Categoria;
import br.com.siscomanda.repository.dao.CategoriaDAO;
import br.com.siscomanda.util.JSFUtil;
import br.com.siscomanda.util.StringUtil;

public class CategoriaService implements Serializable {


	private static final long serialVersionUID = -2167240798275553918L;
		
	@Inject
	private CategoriaDAO dao;
	
	public List<Categoria> pesquisar(Categoria categoria) throws NapuleException {
		List<Categoria> list = null;
		if(list == null && !categoria.getDescricao().isEmpty()) {
			list = dao.porDescricao(categoria.getDescricao(), Categoria.class);
		}
		
		if(list == null) {
			list = todos();
		}
				
		return list;
	}
	
	@Transactional
	public Categoria salvar(Categoria categoria) throws NapuleException {
		categoria = validacao(categoria);
		if(categoria.isNovo()) {
			categoria = dao.salvar(categoria);
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro salvo com sucesso.");
			
			return categoria;
		}
		
		JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro alterado com sucesso.");
		return dao.salvar(categoria);
	}
	
	private Categoria validacao(Categoria categoria) throws NapuleException {
		if(StringUtil.isEmpty(categoria.getDescricao())) {
			throw new NapuleException("É necessário informar uma descrição para a categoria.!");
		}
		
		if(categoria.isNovo()) {
			if(dao.isExists(categoria.getDescricao(), Categoria.class)) {
				throw new NapuleException("Essa categoria já se encontra cadastro no sistema.!");
			}
		}
		
		return categoria;
	}
	
	public List<Categoria> todos() {
		return dao.todos(Categoria.class);
	}
	
	@Transactional
	public void remover(List<Categoria> categorias) throws NapuleException {
		if(categorias == null || categorias.isEmpty()) {
			throw new NapuleException("Selecione pelo menos 1 registro para ser excluído.!");
		}
		
		try {
			for(Categoria categoria : categorias) {
				dao.remover(Categoria.class, categoria.getId());
			}
		}
		catch(NapuleException e) {
			throw new NapuleException(e.getMessage(), e);
		}
	}
}
