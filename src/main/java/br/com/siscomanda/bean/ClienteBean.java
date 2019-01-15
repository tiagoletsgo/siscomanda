package br.com.siscomanda.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.siscomanda.base.bean.BaseBean;
import br.com.siscomanda.exception.NapuleException;
import br.com.siscomanda.model.Cliente;
import br.com.siscomanda.service.ClienteService;
import br.com.siscomanda.util.JSFUtil;

@Named
@ViewScoped
public class ClienteBean extends BaseBean<Cliente> implements Serializable {

	private static final long serialVersionUID = -3066996558123931260L;
	
	@Inject
	private ClienteService clienteService;
	
	private List<Cliente> clientes;
	
	private Cliente clienteSelecionado;
	
	private List<Cliente> clientesSelecionados;
	
	@Override 
	public void init() {
	}
	
	public void btnRemover() {
		try {
			clienteService.remover(getClientesSelecionados());
			getElements().removeAll(getClientesSelecionados());
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro(s) removido(s) com sucesso.");
		} catch (NapuleException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao remover. " + e.getMessage());
		}
	}
	
	public void btnSalvar() {
		try {			
			clienteService.salvar(getEntity());
			setEntity(new Cliente());
			
			if(getEstadoViewBean().getUpdate()) {
				setElements(clienteService.todos());
				getEstadoViewBean().setUpdate(false);
			}
		}
		catch(NapuleException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar. " + e.getMessage());
		}
	}
	
	public void btnPesquisar() {
		try {	
			setElements(clienteService.pesquisar(getEntity()));
		}
		catch(NapuleException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_WARN, e.getMessage());
		}
	}
	
	@Override
	public void btnEditar(Cliente cliente) {
		try {			
			cliente = clienteService.porCodigo(cliente);
			setEntity(cliente);
			
			getEstadoViewBean().setCurrentView(false, true, false, true);
		}
		catch(NapuleException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}
	
	@Override
	protected void beforeSearch() {
		if(getEstadoViewBean().getSearch()) {
			setElements(clienteService.todos());
		}
		setEntity(new Cliente());
	}
		
	public List<Cliente> getClientes() {
		return clientes;
	}

	public Cliente getClienteSelecionado() {
		return clienteSelecionado;
	}

	public void setClienteSelecionado(Cliente clienteSelecionado) {
		this.clienteSelecionado = clienteSelecionado;
	}

	public List<Cliente> getClientesSelecionados() {
		return clientesSelecionados;
	}

	public void setClientesSelecionados(List<Cliente> clientesSelecionados) {
		this.clientesSelecionados = clientesSelecionados;
	}
}
