package br.com.siscomanda.service;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import br.com.siscomanda.config.jpa.Transactional;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Categoria;
import br.com.siscomanda.model.SubCategoria;
import br.com.siscomanda.repository.dao.SubCategoriaDAO;
import br.com.siscomanda.util.JSFUtil;
import br.com.siscomanda.util.StringUtil;

public class SubCategoriaService implements Serializable {


	private static final long serialVersionUID = -2167240798275553918L;
		
	@Inject
	private SubCategoriaDAO dao;
	
	public List<SubCategoria> pesquisar(SubCategoria subCategoria) throws SiscomandaException {
		List<SubCategoria> list = null;
		if(list == null && !subCategoria.getDescricao().isEmpty()) {
			list = dao.porDescricao(subCategoria.getDescricao());
		}
		
		if(list == null) {
			list = todos();
		}
				
		return list;
	}
	
	public List<SubCategoria> pesquisar(Categoria categoria) throws SiscomandaException {
		return dao.porCategoria(categoria);
	}
	
	@Transactional
	public SubCategoria salvar(SubCategoria subCategoria) throws SiscomandaException {
		subCategoria = validacao(subCategoria);
		if(subCategoria.isNovo()) {
			subCategoria = dao.salvar(subCategoria);
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro salvo com sucesso.");
			
			return subCategoria;
		}
		
		JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro alterado com sucesso.");
		return dao.salvar(subCategoria);
	}
	
	private SubCategoria validacao(SubCategoria subCategoria) throws SiscomandaException {
		if(StringUtil.isEmpty(subCategoria.getDescricao())) {
			throw new SiscomandaException("É necessário informar uma descrição.!");
		}
		
		if(Objects.isNull(subCategoria.getCategoria())) {
			throw new SiscomandaException("É necessário informar a categoria.!");
		}
		
		if(subCategoria.isNovo()) {
			if(dao.isExists(subCategoria)) {
				throw new SiscomandaException("Essa categoria já se encontra cadastro no sistema.!");
			}
		}
		
		return subCategoria;
	}
	
	public List<SubCategoria> todos() {
		return dao.todos(SubCategoria.class);
	}
	
	@Transactional
	public void remover(List<SubCategoria> subCategorias) throws SiscomandaException {
		if(subCategorias == null || subCategorias.isEmpty()) {
			throw new SiscomandaException("Selecione pelo menos 1 registro para ser excluído.!");
		}
		
		try {
			for(SubCategoria sub : subCategorias) {
				dao.remover(SubCategoria.class, sub.getId());
			}
		}
		catch(SiscomandaException e) {
			throw new SiscomandaException(e.getMessage(), e);
		}
	}
}
