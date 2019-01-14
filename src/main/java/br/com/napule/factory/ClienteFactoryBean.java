package br.com.napule.factory;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.napule.model.Cliente;
import br.com.napule.service.ClienteService;

@Named
@ViewScoped
public class ClienteFactoryBean implements Serializable {

	private static final long serialVersionUID = 1855478491934173312L;
	
	@Inject
	private ClienteService service;
	
	private List<Cliente> clientes;
	
	@PostConstruct
	public void init() {
		this.clientes = service.todos();
	}
	
	public List<Cliente> getClientes() {
		return clientes;
	}
}
