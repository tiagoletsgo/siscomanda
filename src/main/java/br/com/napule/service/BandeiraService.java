package br.com.napule.service;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import br.com.napule.config.jpa.Transactional;
import br.com.napule.config.jsf.JSFUtil;
import br.com.napule.exception.NapuleException;
import br.com.napule.model.Bandeira;
import br.com.napule.repository.dao.BandeiraDAO;
import br.com.napule.util.StringUtil;

public class BandeiraService implements Serializable {


	private static final long serialVersionUID = -2167240798275553918L;
		
	@Inject
	private BandeiraDAO dao;
	
	public List<Bandeira> pesquisar(Bandeira bandeira) throws NapuleException {
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
	public Bandeira salvar(Bandeira bandeira) throws NapuleException {
		bandeira = validacao(bandeira);
		if(bandeira.isNovo()) {
			bandeira = dao.salvar(bandeira);
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro salvo com sucesso.");
			
			return bandeira;
		}
		
		JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro alterado com sucesso.");
		return dao.salvar(bandeira);
	}
	
	private Bandeira validacao(Bandeira bandeira) throws NapuleException {
		if(StringUtil.isEmpty(bandeira.getDescricao())) {
			throw new NapuleException("É necessário informar uma descrição para a bandeira.!");
		}
		
		if(bandeira.isNovo()) {
			if(dao.isExists(bandeira.getDescricao(), Bandeira.class)) {
				throw new NapuleException("Essa bandeira já se encontra cadastro no sistema.!");
			}
		}
		
		return bandeira;
	}
	
	public List<Bandeira> todos() {
		return dao.todos(Bandeira.class);
	}
	
	@Transactional
	public void remover(List<Bandeira> bandeiras) throws NapuleException {
		if(bandeiras == null || bandeiras.isEmpty()) {
			throw new NapuleException("Selecione pelo menos 1 registro para ser excluído.!");
		}
		
		try {
			for(Bandeira bandeira: bandeiras) {
				dao.remover(Bandeira.class, bandeira.getId());
			}
		}
		catch(NapuleException e) {
			throw new NapuleException(e.getMessage(), e);
		}
	}
}
