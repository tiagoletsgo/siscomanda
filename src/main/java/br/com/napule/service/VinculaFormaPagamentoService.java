package br.com.napule.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import br.com.napule.config.jpa.Transactional;
import br.com.napule.config.jsf.JSFUtil;
import br.com.napule.exception.NapuleException;
import br.com.napule.model.Bandeira;
import br.com.napule.model.FormaPagamento;
import br.com.napule.model.VinculaFormaPagamento;
import br.com.napule.repository.dao.VinculaFormaPagamentoDAO;

public class VinculaFormaPagamentoService implements Serializable {

	private static final long serialVersionUID = 197295647249164436L;
	
	@Inject
	private VinculaFormaPagamentoDAO dao;
	
	@Transactional
	public void salvar(List<VinculaFormaPagamento> vinculaFormasPagamento, FormaPagamento formaPagamento) throws NapuleException {
		if(formaPagamento == null) {
			throw new NapuleException("Selecione uma forma de pagamento.");
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
