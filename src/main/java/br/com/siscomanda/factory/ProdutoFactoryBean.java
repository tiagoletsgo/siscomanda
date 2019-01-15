package br.com.siscomanda.factory;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.siscomanda.model.Produto;
import br.com.siscomanda.service.ProdutoService;

@Named
@ViewScoped
public class ProdutoFactoryBean implements Serializable {

	private static final long serialVersionUID = -2485891672023525149L;
	
	private List<Produto> produtos;
	
	@Inject
	private ProdutoService service;
	
	@PostConstruct
	public void init() {
		produtos = service.todos();
	}
	
	public List<Produto> getProdutos() {
		return produtos;
	}
}
