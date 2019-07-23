package br.com.siscomanda.service;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import br.com.siscomanda.config.jpa.Transactional;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Cliente;
import br.com.siscomanda.repository.dao.ClienteDAO;
import br.com.siscomanda.util.JSFUtil;
import br.com.siscomanda.util.StringUtil;

public class ClienteService implements Serializable {


	private static final long serialVersionUID = -2167240798275553918L;
		
	@Inject
	private ClienteDAO dao;
	
	public List<Cliente> pesquisar(Cliente cliente) throws SiscomandaException {
		List<Cliente> list = null;
		verificaCampoSelecionado(cliente);
		list = dao.buscaPor(cliente);				
		return list;
	}
	
	private void verificaCampoSelecionado(Cliente cliente) throws SiscomandaException {
		StringUtil.maisDeUmCampoPreenchido(Arrays.asList(
					cliente.getNomeCompleto(),
					cliente.getRg(),
					cliente.getCpf(),
					cliente.getTelefoneCelular()
				));
	}
	
	@Transactional
	public Cliente salvar(Cliente cliente) throws SiscomandaException {
		cliente = validacao(cliente);
		if(cliente.isNovo()) {
			cliente = dao.salvar(cliente);
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro salvo com sucesso.");
			
			return cliente;
		}
		
		JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro alterado com sucesso.");
		return dao.salvar(cliente);
	}
	
	public Cliente porCodigo(Cliente cliente) throws SiscomandaException {
		return dao.porCodigo(cliente);
	}
	
	private Cliente validacao(Cliente cliente) throws SiscomandaException {
		if(StringUtil.isEmpty(cliente.getNomeCompleto())) {
			throw new SiscomandaException("É necessário informar o nome do cliente.!");
		}
		
		if(StringUtil.isEmpty(cliente.getEndereco().getEndereco()) || StringUtil.isEmpty(cliente.getEndereco().getBairro())
				|| StringUtil.isEmpty(cliente.getEndereco().getCidade())) {
			throw new SiscomandaException("É necessário informar endereço, bairro e cidade.!");
		}

		if(StringUtil.isEmpty(cliente.getTelefoneCelular()) && StringUtil.isEmpty(cliente.getTelefoneFixo())) {
			throw new SiscomandaException("É necessário informar pelo menos um número para contato.!");
		}
		
		if(!Objects.nonNull(cliente.getServico())) {
			throw new SiscomandaException("É necessário informar a taxa de serviço.!");
		}
		
		if(cliente.isNovo()) {
			if(dao.isExists(cliente)) {
				throw new SiscomandaException("Esse cliente já se encontra cadastro no sistema.!");
			}
		}
		
		cliente.setTelefoneCelular(StringUtil.somenteAlfanumericoSemEspaco(cliente.getTelefoneCelular()));
		cliente.setTelefoneFixo(StringUtil.somenteAlfanumericoSemEspaco(cliente.getTelefoneFixo()));
		cliente.setRg(StringUtil.somenteAlfanumericoSemEspaco(cliente.getRg()));
		cliente.setCpf(StringUtil.somenteAlfanumericoSemEspaco(cliente.getCpf()));
		cliente.getEndereco().setCep(StringUtil.somenteAlfanumericoSemEspaco(cliente.getEndereco().getCep()));
		return cliente;
	}
	
	public List<Cliente> todos() {
		return dao.todos();
	}
	
	@Transactional
	public void remover(List<Cliente> clientes) throws SiscomandaException {
		if(clientes == null || clientes.isEmpty()) {
			throw new SiscomandaException("Selecione pelo menos 1 registro para ser excluído.!");
		}
		
		try {
			for(Cliente cliente : clientes) {
				dao.remover(Cliente.class, cliente.getId());
			}
		}
		catch(SiscomandaException e) {
			throw new SiscomandaException(e.getMessage(), e);
		}
	}
}
