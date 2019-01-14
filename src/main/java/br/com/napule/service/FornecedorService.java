package br.com.napule.service;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import br.com.napule.config.jpa.Transactional;
import br.com.napule.config.jsf.JSFUtil;
import br.com.napule.exception.NapuleException;
import br.com.napule.model.Fornecedor;
import br.com.napule.repository.dao.FornecedorDAO;
import br.com.napule.util.StringUtil;

public class FornecedorService implements Serializable {


	private static final long serialVersionUID = -2167240798275553918L;
		
	@Inject
	private FornecedorDAO dao;
	
	public List<Fornecedor> pesquisar(Fornecedor fornecedor) throws NapuleException {
		List<Fornecedor> list = null;
		verificaCampoSelecionado(fornecedor);
		list = dao.buscaPor(fornecedor);
		return list;
	}
	
	private void verificaCampoSelecionado(Fornecedor fornecedor) throws NapuleException {
		StringUtil.maisDeUmCampoPreenchido(Arrays.asList(
					fornecedor.getRazaoSocial(),
					fornecedor.getNomeFantasia(),
					fornecedor.getCpfCnpj()
				));
	}
	
	@Transactional
	public Fornecedor salvar(Fornecedor fornecedor) throws NapuleException {
		fornecedor = validacao(fornecedor);
		if(fornecedor.isNovo()) {
			fornecedor = dao.salvar(fornecedor);
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro salvo com sucesso.");
			
			return fornecedor;
		}
		
		JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro alterado com sucesso.");
		return dao.salvar(fornecedor);
	}
	
	private Fornecedor validacao(Fornecedor fornecedor) throws NapuleException {
		if(!Objects.nonNull(fornecedor.getTipoPessoa())) {
			throw new NapuleException("É necessário informar o tipo de pessoa.!");
		}
		
		if(StringUtil.isEmpty(fornecedor.getRazaoSocial())) {
			throw new NapuleException("É necessário informar a razão social.!");
		}
		
		if(StringUtil.isEmpty(fornecedor.getCpfCnpj())) {
			throw new NapuleException("É necessário informar o cpf/cnpj.!");
		}
		
		if(fornecedor.isNovo()) {
			if(dao.isExists(fornecedor)) {
				throw new NapuleException("Esse fornecedor já se encontra cadastro no sistema.!");
			}
		}
		
		fornecedor.setTelefoneCelular(StringUtil.somenteAlfanumericoSemEspaco(fornecedor.getTelefoneCelular()));
		fornecedor.setTelefoneFixo(StringUtil.somenteAlfanumericoSemEspaco(fornecedor.getTelefoneFixo()));
		fornecedor.setCpfCnpj(StringUtil.somenteAlfanumericoSemEspaco(fornecedor.getCpfCnpj()));
		fornecedor.getEndereco().setCep(StringUtil.somenteAlfanumericoSemEspaco(fornecedor.getEndereco().getCep()));
		return fornecedor;
	}
	
	public List<Fornecedor> todos() {
		return dao.todos(Fornecedor.class);
	}
	
	@Transactional
	public void remover(List<Fornecedor> fornecedores) throws NapuleException {
		if(fornecedores == null || fornecedores.isEmpty()) {
			throw new NapuleException("Selecione pelo menos 1 registro para ser excluído.!");
		}
		
		try {
			for(Fornecedor fornecedor : fornecedores) {
				dao.remover(Fornecedor.class, fornecedor.getId());
			}
		}
		catch(NapuleException e) {
			throw new NapuleException(e.getMessage(), e);
		}
	}
}
