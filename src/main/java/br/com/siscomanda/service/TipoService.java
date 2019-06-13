package br.com.siscomanda.service;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import br.com.siscomanda.config.jpa.Transactional;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Tipo;
import br.com.siscomanda.repository.dao.TipoDAO;
import br.com.siscomanda.util.JSFUtil;
import br.com.siscomanda.util.StringUtil;

public class TipoService implements Serializable {

	private static final long serialVersionUID = 3080937545668673677L;
	
	@Inject
	private TipoDAO dao;
	
	public List<Tipo> pesquisar(Tipo tipo) throws SiscomandaException {
		List<Tipo> list = null;
		if(list == null && !tipo.getDescricao().isEmpty()) {
			list = dao.porDescricao(tipo.getDescricao(), Tipo.class);
		}
		
		if(list == null) {
			list = todos();
		}
				
		return list;
	}
	
	@Transactional
	public Tipo salvar(Tipo tipo) throws SiscomandaException {
		tipo = validacao(tipo);
		if(tipo.isNovo()) {
			tipo = dao.salvar(tipo);
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro salvo com sucesso.");
			
			return tipo;
		}
		
		JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro alterado com sucesso.");
		return dao.salvar(tipo);
	}
	
	private Tipo validacao(Tipo tipo) throws SiscomandaException {
		if(StringUtil.isEmpty(tipo.getDescricao())) {
			throw new SiscomandaException("É necessário informar uma descrição para o tipo.!");
		}
		
		if(tipo.isNovo()) {
			if(dao.isExists(tipo.getDescricao(), Tipo.class)) {
				throw new SiscomandaException("Esse tipo já se encontra cadastro no sistema.!");
			}
		}
		
		return tipo;
	}
	
	public List<Tipo> todos() {
		return dao.todos(Tipo.class);
	}
	
	@Transactional
	public void remover(List<Tipo> tipos) throws SiscomandaException {
		if(tipos == null || tipos.isEmpty()) {
			throw new SiscomandaException("Selecione pelo menos 1 registro para ser excluído.!");
		}
		
		try {
			for(Tipo tipo : tipos) {
				dao.remover(Tipo.class, tipo.getId());
			}
		}
		catch(SiscomandaException e) {
			throw new SiscomandaException(e.getMessage(), e);
		}
	}
}
