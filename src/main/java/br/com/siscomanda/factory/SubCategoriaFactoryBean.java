package br.com.siscomanda.factory;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.siscomanda.model.SubCategoria;
import br.com.siscomanda.service.SubCategoriaService;

@Named
@ViewScoped
public class SubCategoriaFactoryBean implements Serializable {
	
	private static final long serialVersionUID = 3016582712318244934L;

	@Inject
	private SubCategoriaService service;
	
	private List<SubCategoria> subCategorias;
	
	@PostConstruct
	public void init() {
		this.subCategorias = service.todos();
	}
	
	public List<SubCategoria> getSubCategorias() {
		return subCategorias;
	}
}
