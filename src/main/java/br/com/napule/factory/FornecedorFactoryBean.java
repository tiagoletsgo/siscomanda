package br.com.napule.factory;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.napule.model.Fornecedor;
import br.com.napule.service.FornecedorService;

@Named
@ViewScoped
public class FornecedorFactoryBean implements Serializable {

	private static final long serialVersionUID = 6332701150659904649L;
	
	@Inject
	private FornecedorService service;
	
	private List<Fornecedor> fornecedores;
	
	@PostConstruct
	public void init() {
		this.fornecedores = service.todos();
	}
	
	public List<Fornecedor> getFornecedores() {
		return fornecedores;
	}
}
