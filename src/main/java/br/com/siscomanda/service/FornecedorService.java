package br.com.siscomanda.service;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import br.com.siscomanda.config.jpa.Transactional;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Fornecedor;
import br.com.siscomanda.repository.dao.FornecedorDAO;
import br.com.siscomanda.util.JSFUtil;
import br.com.siscomanda.util.StringUtil;

public class FornecedorService implements Serializable {


	private static final long serialVersionUID = -2167240798275553918L;
		
	@Inject
	private FornecedorDAO dao;
	
	public List<Fornecedor> pesquisar(Fornecedor fornecedor) throws SiscomandaException {
		List<Fornecedor> list = null;
		verificaCampoSelecionado(fornecedor);
		list = dao.buscaPor(fornecedor);
		return list;
	}
	
	private void verificaCampoSelecionado(Fornecedor fornecedor) throws SiscomandaException {
		StringUtil.maisDeUmCampoPreenchido(Arrays.asList(
					fornecedor.getRazaoSocial(),
					fornecedor.getNomeFantasia(),
					fornecedor.getCpfCnpj()
				));
	}
	
	@Transactional
	public Fornecedor salvar(Fornecedor fornecedor) throws SiscomandaException {
		fornecedor = validacao(fornecedor);
		if(fornecedor.isNovo()) {
			fornecedor = dao.salvar(fornecedor);
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro salvo com sucesso.");
			
			return fornecedor;
		}
		
		JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro alterado com sucesso.");
		return dao.salvar(fornecedor);
	}
	
	private Fornecedor validacao(Fornecedor fornecedor) throws SiscomandaException {
		if(!Objects.nonNull(fornecedor.getTipoPessoa())) {
			throw new SiscomandaException("É necessário informar o tipo de pessoa.!");
		}
		
		if(StringUtil.isEmpty(fornecedor.getRazaoSocial())) {
			throw new SiscomandaException("É necessário informar a razão social.!");
		}
		
		if(StringUtil.isEmpty(fornecedor.getCpfCnpj())) {
			throw new SiscomandaException("É necessário informar o cpf/cnpj.!");
		}
		
		if(fornecedor.isNovo()) {
			if(dao.isExists(fornecedor)) {
				throw new SiscomandaException("Esse fornecedor já se encontra cadastro no sistema.!");
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
	public void remover(List<Fornecedor> fornecedores) throws SiscomandaException {
		if(fornecedores == null || fornecedores.isEmpty()) {
			throw new SiscomandaException("Selecione pelo menos 1 registro para ser excluído.!");
		}
		
		try {
			for(Fornecedor fornecedor : fornecedores) {
				dao.remover(Fornecedor.class, fornecedor.getId());
			}
		}
		catch(SiscomandaException e) {
			throw new SiscomandaException(e.getMessage(), e);
		}
	}
}
