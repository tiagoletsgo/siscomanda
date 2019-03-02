package br.com.siscomanda.service;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import br.com.siscomanda.config.jpa.Transactional;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Adicional;
import br.com.siscomanda.repository.dao.AdicionalDAO;
import br.com.siscomanda.util.JSFUtil;

public class AdicionalService implements Serializable {

	private static final long serialVersionUID = 6750595631115213547L;
	
	@Inject
	private AdicionalDAO dao;
	
	@Transactional
	public Adicional salvar(Adicional adicional) throws SiscomandaException {
		validacao(adicional);
		if(adicional.isNovo()) {			
			adicional = dao.salvar(adicional);
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro salvo com sucesso.");
			
			return adicional;
		}
		
		JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro alterado com sucesso.");
		return dao.salvar(adicional);
	}
	
	@Transactional
	public void remover(List<Adicional> adicionais) throws SiscomandaException {
		if(adicionais == null || adicionais.isEmpty()) {
			throw new SiscomandaException("Selecione pelo menos 1 registro para ser excluído.!");
		}
		
		try {
			for(Adicional adicional : adicionais) {
				dao.removeAdicionalCategoriaPorCodigo(adicional.getId());
				dao.removeAdicionalPorCodigo(adicional.getId());
			}
		}
		catch(SiscomandaException e) {
			throw new SiscomandaException(e.getMessage(), e);
		}
	}
	
	public List<Adicional> pesquisar(Adicional adicional) throws SiscomandaException {
		List<Adicional> adicionais = null;
		adicionais = dao.buscaPor(adicional.getDescricao());	
		return adicionais;
	}
	
	public List<Adicional> todos() {
		return dao.buscaPor(null);
	}
	
	private void validacao(Adicional adicional) throws SiscomandaException {
		if(adicional.getDescricao() == null || adicional.getDescricao().isEmpty()) {
			throw new SiscomandaException("Necessário informar uma descrição.");
		}
		
		if(adicional.getCategorias() == null || adicional.getCategorias().isEmpty()) {
			throw new SiscomandaException("Necessário informar ao menos uma categoria.");
		}
		
		if(adicional.getPrecoCusto() <= new Double("0") || adicional.getPrecoVenda() <= new Double("0")) {
			throw new SiscomandaException("Não é permitido um valor menor ou igual a zero para preço de venda / preço de custo.");
		}
		
		if(adicional.getPrecoCusto() > adicional.getPrecoVenda()) {
			throw new SiscomandaException("Preço de custo não pode ser maior que preço de venda.");
		}
		
		if(adicional.getFatorMedida() == null) {
			throw new SiscomandaException("Necessário informar um fator de medida.");
		}
	}
}
