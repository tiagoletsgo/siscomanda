package br.com.napule.factory;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.napule.model.Categoria;
import br.com.napule.service.CategoriaService;

@Named
@ViewScoped
public class CategoriaFactoryBean implements Serializable {

	private static final long serialVersionUID = 3889546461177319890L;
	
	@Inject
	private CategoriaService service;
	
	private List<Categoria> categorias;
	
	@PostConstruct
	public void init() {
		this.categorias = service.todos();
	}
	
	public List<Categoria> getCategorias() {
		return categorias;
	}

}
