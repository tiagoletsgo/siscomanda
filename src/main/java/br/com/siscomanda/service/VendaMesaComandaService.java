package br.com.siscomanda.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.siscomanda.base.service.VendaService;
import br.com.siscomanda.repository.dao.VendaMesaComandaDAO;

public class VendaMesaComandaService extends VendaService implements Serializable {

	private static final long serialVersionUID = -7365230528253341931L;
	
	@Inject
	private VendaMesaComandaDAO dao;
	
	@Inject
	private DefinicaoGeralService definicaoGeralService;
	
	public List<Integer> geraMesasComandas() {
		List<Integer> mesas = new ArrayList<>();
		int qtdMesasComandas = definicaoGeralService.carregaDefinicaoSistema().getQtdMesaComanda();
		for(int i = 0; i < qtdMesasComandas; i++) {
			mesas.add(i + 1);
		}
		return mesas;
	}
	
	public Double getTaxaServico() {
		Double valor = definicaoGeralService.carregaDefinicaoSistema().getTaxaServico();
		return (valor / 100);
	}
}
