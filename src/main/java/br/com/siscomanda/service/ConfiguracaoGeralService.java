package br.com.siscomanda.service;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import br.com.siscomanda.config.jpa.Transactional;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.ConfiguracaoGeral;
import br.com.siscomanda.repository.dao.ConfiguracaoGeralDAO;
import br.com.siscomanda.util.JSFUtil;
import br.com.siscomanda.util.StringUtil;

public class ConfiguracaoGeralService implements Serializable {

	private static final long serialVersionUID = -214593377317757605L;

	@Inject
	private ConfiguracaoGeralDAO dao;
		
	@Transactional
	public ConfiguracaoGeral salvar(ConfiguracaoGeral definicaoGeral) throws SiscomandaException {
		validacao(definicaoGeral);
		if(definicaoGeral.isNovo()) {
			definicaoGeral = dao.salvar(definicaoGeral);
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro salvo com sucesso.");
			
			return definicaoGeral;
		}
		
		JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro alterado com sucesso.");
		return dao.salvar(definicaoGeral);
	}
	
	public ConfiguracaoGeral definicaoSistema() {
		List<ConfiguracaoGeral> definicoesSistema = dao.todos(ConfiguracaoGeral.class);
		if(definicoesSistema != null && !definicoesSistema.isEmpty()) {
			return dao.todos(ConfiguracaoGeral.class).get(0);
		}
		return new ConfiguracaoGeral();
	}
	
	private void validacao(ConfiguracaoGeral definicaoGeral) throws SiscomandaException {
		if(definicaoGeral == null) {
			throw new SiscomandaException("Por gentileza preencha corretamente os campos.");
		}
		
		if(StringUtil.isEmpty(definicaoGeral.getNomeFantasia())) {
			throw new SiscomandaException("É necessário informar o nome fantasia.!");
		}
		
		if(StringUtil.isEmpty(definicaoGeral.getRazaoSocial())) {
			throw new SiscomandaException("É necessário informar o razão social.!");
		}
		
		if(StringUtil.isEmpty(definicaoGeral.getCnpj())) {
			throw new SiscomandaException("É necessário informar o cnpj.!");
		}
		
		if(StringUtil.isEmpty(definicaoGeral.getEndereco().getEndereco())) {
			throw new SiscomandaException("É necessário informar o endereço.!");
		}
		
		if(StringUtil.isEmpty(definicaoGeral.getEndereco().getBairro())) {
			throw new SiscomandaException("É necessário informar o bairro.!");
		}
		
		if(StringUtil.isEmpty(definicaoGeral.getEndereco().getCidade())) {
			throw new SiscomandaException("É necessário informar a cidade.!");
		}
		
		if(StringUtil.isEmpty(definicaoGeral.getEndereco().getUf())) {
			throw new SiscomandaException("É necessário informar o uf.!");
		}
		
		if(StringUtil.isEmpty(definicaoGeral.getFonePrincipal())) {
			throw new SiscomandaException("É necessário informar o telefone principal.!");
		}
		
		if(definicaoGeral.getQtdMesaComanda() == 0) {
			throw new SiscomandaException("A quantidade zero não é permitida para o sistema de mesa / comanda.!");
		}
		
		if(definicaoGeral.getPermiteQuantoSabores() == null || definicaoGeral.getPermiteQuantoSabores() == 0) {
			throw new SiscomandaException("Zero/Nulo não é permitido para este campo!");
		}
	}
}
