package br.com.siscomanda.service;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import br.com.siscomanda.config.jpa.Transactional;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Tamanho;
import br.com.siscomanda.model.Tipo;
import br.com.siscomanda.repository.dao.TamanhoDAO;
import br.com.siscomanda.util.JSFUtil;
import br.com.siscomanda.util.StringUtil;

public class TamanhoService implements Serializable {

	private static final long serialVersionUID = -5563004072918414575L;
	
	@Inject
	private TamanhoDAO dao;
	
	public List<Tamanho> pesquisar(Tamanho tamanho) throws SiscomandaException {
		List<Tamanho> list = null;
		if(list == null && !tamanho.getDescricao().isEmpty()) {
			list = dao.porDescricao(tamanho.getDescricao(), Tamanho.class);
		}
		
		if(list == null) {
			list = todos();
		}
				
		return list;
	}
	
	public List<Tamanho> tamanhoPorTipo(Tipo tipo) {
		return dao.tamanhoPorTipo(tipo);
	}
	
	@Transactional
	public Tamanho salvar(Tamanho tamanho) throws SiscomandaException {
		tamanho = validacao(tamanho);
		if(tamanho.isNovo()) {
			tamanho = dao.salvar(tamanho);
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro salvo com sucesso.");
			
			return tamanho;
		}
		
		JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro alterado com sucesso.");
		return dao.salvar(tamanho);
	}
	
	private Tamanho validacao(Tamanho tamanho) throws SiscomandaException {
		if(StringUtil.isEmpty(tamanho.getDescricao())) {
			throw new SiscomandaException("É necessário informar uma descrição para o tamanho.!");
		}
		
		
		return tamanho;
	}
	
	public List<Tamanho> todos() {
		return dao.todos(Tamanho.class);
	}
	
	@Transactional
	public void remover(List<Tamanho> tamanhos) throws SiscomandaException {
		if(tamanhos == null || tamanhos.isEmpty()) {
			throw new SiscomandaException("Selecione pelo menos 1 registro para ser excluído.!");
		}
		
		try {
			for(Tamanho tamanho : tamanhos) {
				dao.remover(Tamanho.class, tamanho.getId());
			}
		}
		catch(SiscomandaException e) {
			throw new SiscomandaException(e.getMessage(), e);
		}
	}
}
