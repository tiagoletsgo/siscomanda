package br.com.siscomanda.factory;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.siscomanda.model.Servico;
import br.com.siscomanda.service.ServicoService;

@Named
@ViewScoped
public class ServicoFactoryBean implements Serializable {

	private static final long serialVersionUID = 3889546461177319890L;
	
	@Inject
	private ServicoService service;
	
	private List<Servico> servicos;
	
	@PostConstruct
	public void init() {
		this.servicos = service.todos();
	}
	
	public List<Servico> getServicos() {
		return servicos;
	}

}
