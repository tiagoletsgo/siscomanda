package br.com.siscomanda.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import br.com.siscomanda.config.jpa.Transactional;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Bandeira;
import br.com.siscomanda.model.FormaPagamento;
import br.com.siscomanda.model.VinculaFormaPagamento;
import br.com.siscomanda.repository.dao.VinculaFormaPagamentoDAO;
import br.com.siscomanda.util.JSFUtil;

public class VinculaFormaPagamentoService implements Serializable {

	private static final long serialVersionUID = 197295647249164436L;
	
	@Inject
	private VinculaFormaPagamentoDAO dao;
	
	@Transactional
	public void salvar(List<VinculaFormaPagamento> vinculaFormasPagamento, FormaPagamento formaPagamento) throws SiscomandaException {
		if(formaPagamento == null) {
			throw new SiscomandaException("Selecione uma forma de pagamento.");
		}
		
		for(VinculaFormaPagamento vinculaFormaPagamento : vinculaFormasPagamento) {
			vinculaFormaPagamento.setFormaPagamento(formaPagamento);
			dao.salvar(vinculaFormaPagamento);
		}
		
		JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro salvo com sucesso.");
	}
	
	public List<VinculaFormaPagamento> vinculaFormasPagamento(List<Bandeira> bandeiras, FormaPagamento formaPagamento) {
		List<VinculaFormaPagamento> vinculaFormasPagamento = new ArrayList<>();
		vinculaFormasPagamento = porFormaPagamento(bandeiras, formaPagamento);
		
		if(vinculaFormasPagamento.isEmpty()) {
			for(Bandeira bandeira : bandeiras) {
				VinculaFormaPagamento vinculaFormaPagamento = new VinculaFormaPagamento();
				vinculaFormaPagamento.setBandeira(bandeira);
				vinculaFormaPagamento.setVincular(false);
				
				vinculaFormasPagamento.add(vinculaFormaPagamento);
			}
		}
		
		return vinculaFormasPagamento;
	}
	
	private List<VinculaFormaPagamento> porFormaPagamento(List<Bandeira> bandeiras, FormaPagamento formaPagamento) {
		List<VinculaFormaPagamento> vinculaFormasPagamento = new ArrayList<>();
		vinculaFormasPagamento = dao.porFormaPagamento(formaPagamento);
		boolean  exists = false;
		
		for(Bandeira bandeira : bandeiras) {
			for(VinculaFormaPagamento vinculaFormaPagamento : vinculaFormasPagamento) {
				if(vinculaFormaPagamento.getBandeira().equals(bandeira)) {
					exists = true;
					break;
				}
				exists = false;
			}
			
			if(!exists) {
				VinculaFormaPagamento vinculaFormaPagamento = new VinculaFormaPagamento();
				vinculaFormaPagamento.setBandeira(bandeira);
				vinculaFormaPagamento.setVincular(false);
				vinculaFormasPagamento.add(vinculaFormaPagamento);
			}
		}
		
		return vinculaFormasPagamento;
	}
}
