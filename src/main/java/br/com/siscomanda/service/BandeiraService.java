package br.com.siscomanda.service;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import br.com.siscomanda.config.jpa.Transactional;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Bandeira;
import br.com.siscomanda.repository.dao.BandeiraDAO;
import br.com.siscomanda.util.JSFUtil;
import br.com.siscomanda.util.StringUtil;

public class BandeiraService implements Serializable {


	private static final long serialVersionUID = -2167240798275553918L;
		
	@Inject
	private BandeiraDAO dao;
	
	public List<Bandeira> pesquisar(Bandeira bandeira) throws SiscomandaException {
		List<Bandeira> list = null;
		if(list == null && !bandeira.getDescricao().isEmpty()) {
			list = dao.porDescricao(bandeira.getDescricao(), Bandeira.class);
		}
		
		if(list == null) {
			list = todos();
		}
				
		return list;
	}
	
	@Transactional
	public Bandeira salvar(Bandeira bandeira) throws SiscomandaException {
		bandeira = validacao(bandeira);
		if(bandeira.isNovo()) {
			bandeira = dao.salvar(bandeira);
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro salvo com sucesso.");
			
			return bandeira;
		}
		
		JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro alterado com sucesso.");
		return dao.salvar(bandeira);
	}
	
	private Bandeira validacao(Bandeira bandeira) throws SiscomandaException {
		if(StringUtil.isEmpty(bandeira.getDescricao())) {
			throw new SiscomandaException("É necessário informar uma descrição para a bandeira.!");
		}
		
		if(bandeira.isNovo()) {
			if(dao.isExists(bandeira.getDescricao(), Bandeira.class)) {
				throw new SiscomandaException("Essa bandeira já se encontra cadastro no sistema.!");
			}
		}
		
		return bandeira;
	}
	
	public List<Bandeira> todos() {
		return dao.todos(Bandeira.class);
	}
	
	@Transactional
	public void remover(List<Bandeira> bandeiras) throws SiscomandaException {
		if(bandeiras == null || bandeiras.isEmpty()) {
			throw new SiscomandaException("Selecione pelo menos 1 registro para ser excluído.!");
		}
		
		try {
			for(Bandeira bandeira: bandeiras) {
				dao.remover(Bandeira.class, bandeira.getId());
			}
		}
		catch(SiscomandaException e) {
			throw new SiscomandaException(e.getMessage(), e);
		}
	}
}
