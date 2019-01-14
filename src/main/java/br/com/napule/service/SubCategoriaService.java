package br.com.napule.service;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import br.com.napule.config.jpa.Transactional;
import br.com.napule.config.jsf.JSFUtil;
import br.com.napule.exception.NapuleException;
import br.com.napule.model.SubCategoria;
import br.com.napule.repository.dao.SubCategoriaDAO;
import br.com.napule.util.StringUtil;

public class SubCategoriaService implements Serializable {


	private static final long serialVersionUID = -2167240798275553918L;
		
	@Inject
	private SubCategoriaDAO dao;
	
	public List<SubCategoria> pesquisar(SubCategoria subCategoria) throws NapuleException {
		List<SubCategoria> list = null;
		if(list == null && !subCategoria.getDescricao().isEmpty()) {
			list = dao.porDescricao(subCategoria.getDescricao());
		}
		
		if(list == null) {
			list = todos();
		}
				
		return list;
	}
	
	@Transactional
	public SubCategoria salvar(SubCategoria subCategoria) throws NapuleException {
		subCategoria = validacao(subCategoria);
		if(subCategoria.isNovo()) {
			subCategoria = dao.salvar(subCategoria);
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro salvo com sucesso.");
			
			return subCategoria;
		}
		
		JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro alterado com sucesso.");
		return dao.salvar(subCategoria);
	}
	
	private SubCategoria validacao(SubCategoria subCategoria) throws NapuleException {
		if(StringUtil.isEmpty(subCategoria.getDescricao())) {
			throw new NapuleException("É necessário informar uma descrição para a categoria.!");
		}
		
		if(subCategoria.isNovo()) {
			if(dao.isExists(subCategoria)) {
				throw new NapuleException("Essa categoria já se encontra cadastro no sistema.!");
			}
		}
		
		return subCategoria;
	}
	
	public List<SubCategoria> todos() {
		return dao.todos(SubCategoria.class);
	}
	
	@Transactional
	public void remover(List<SubCategoria> subCategorias) throws NapuleException {
		if(subCategorias == null || subCategorias.isEmpty()) {
			throw new NapuleException("Selecione pelo menos 1 registro para ser excluído.!");
		}
		
		try {
			for(SubCategoria sub : subCategorias) {
				dao.remover(SubCategoria.class, sub.getId());
			}
		}
		catch(NapuleException e) {
			throw new NapuleException(e.getMessage(), e);
		}
	}
}
