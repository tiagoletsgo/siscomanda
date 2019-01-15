package br.com.siscomanda.factory;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.siscomanda.model.Embalagem;
import br.com.siscomanda.service.EmbalagemService;

@Named
@ViewScoped
public class EmbalagemFactoryBean implements Serializable {

	private static final long serialVersionUID = 6219861732354752902L;
	
	@Inject
	private EmbalagemService service;
	
	private List<Embalagem> embalagens;
	
	@PostConstruct
	public void init() {
		this.embalagens = service.todos();
	}
	
	public List<Embalagem> getEmbalagens() {
		return embalagens;
	}
}
