package br.com.siscomanda.service;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import br.com.siscomanda.config.jpa.Transactional;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.FormaPagamento;
import br.com.siscomanda.repository.dao.FormaPagamentoDAO;
import br.com.siscomanda.util.JSFUtil;
import br.com.siscomanda.util.StringUtil;

public class FormaPagamentoService implements Serializable {


	private static final long serialVersionUID = -2167240798275553918L;
		
	@Inject
	private FormaPagamentoDAO dao;
	
	public List<FormaPagamento> pesquisar(FormaPagamento formaPagamento) throws SiscomandaException {
		List<FormaPagamento> list = null;
		if(list == null && !formaPagamento.getDescricao().isEmpty()) {
			list = dao.porDescricao(formaPagamento.getDescricao(), FormaPagamento.class);
		}
		
		if(list == null) {
			list = todos();
		}
				
		return list;
	}
	
	@Transactional
	public FormaPagamento salvar(FormaPagamento formaPagamento) throws SiscomandaException {
		formaPagamento = validacao(formaPagamento);
		if(formaPagamento.isNovo()) {
			formaPagamento = dao.salvar(formaPagamento);
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro salvo com sucesso.");
			
			return formaPagamento;
		}
		
		JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro alterado com sucesso.");
		return dao.salvar(formaPagamento);
	}
	
	private FormaPagamento validacao(FormaPagamento formaPagamento) throws SiscomandaException {
		if(StringUtil.isEmpty(formaPagamento.getDescricao())) {
			throw new SiscomandaException("É necessário informar uma descrição para forma de pagamento.!");
		}
		
		if(formaPagamento.isNovo()) {
			if(dao.isExists(formaPagamento.getDescricao(), FormaPagamento.class)) {
				throw new SiscomandaException("Essa forma de pagamento já se encontra cadastro no sistema.!");
			}
		}
		
		return formaPagamento;
	}
	
	public List<FormaPagamento> todos() {
		return dao.todos(FormaPagamento.class);
	}
	
	@Transactional
	public void remover(List<FormaPagamento> formasPagamento) throws SiscomandaException {
		if(formasPagamento == null || formasPagamento.isEmpty()) {
			throw new SiscomandaException("Selecione pelo menos 1 registro para ser excluído.!");
		}
		
		try {
			for(FormaPagamento formaPagamento: formasPagamento) {
				dao.remover(FormaPagamento.class, formaPagamento.getId());
			}
		}
		catch(SiscomandaException e) {
			throw new SiscomandaException(e.getMessage(), e);
		}
	}
}
