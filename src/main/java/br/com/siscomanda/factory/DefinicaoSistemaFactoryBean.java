package br.com.siscomanda.factory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.siscomanda.service.DefinicaoGeralService;

@Named
@ViewScoped
public class DefinicaoSistemaFactoryBean implements Serializable {
	
	private static final long serialVersionUID = -4289888959926133557L;

	@Inject
	private DefinicaoGeralService service;
	
	private List<Integer> sistema;
	
	@PostConstruct
	public void init() {
		sistema = new ArrayList<Integer>();
		Integer quantidade = service.carregaDefinicaoSistema().getQtdMesaComanda();
		for(int i = 1; i <= quantidade; i++) {
			sistema.add(i);
		}
	}
	
	public List<Integer> getSistema() {
		return sistema;
	}
}
