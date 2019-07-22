package br.com.siscomanda.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.siscomanda.base.bean.BaseBean;
import br.com.siscomanda.enumeration.ETipoPessoa;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Fornecedor;
import br.com.siscomanda.service.FornecedorService;
import br.com.siscomanda.util.JSFUtil;
import br.com.siscomanda.util.StringUtil;

@Named
@ViewScoped
public class FornecedorBean extends BaseBean<Fornecedor> implements Serializable {

	private static final long serialVersionUID = 7063205029337074599L;

	@Inject
	private FornecedorService fornecedorSevice;
	
	private List<Fornecedor> fornecedores;
	
	private Fornecedor fornecedorSelecionado;
	
	private List<Fornecedor> fornecedoresSelecionados;
	
	@Override 
	public void init() {
	}
	
	public void btnRemover() {
		try {
			fornecedorSevice.remover(getFornecedoresSelecionados());
			getElements().removeAll(getFornecedoresSelecionados());
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro(s) removido(s) com sucesso.");
		} catch (SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao remover. " + e.getMessage());
		}
	}
	
	public void btnSalvar() {
		try {			
			fornecedorSevice.salvar(getEntity());
			setEntity(new Fornecedor());
			getEstadoViewBean().setUpdate(false);
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar. " + e.getMessage());
		}
	}
	
	public void btnPesquisar() {
		try {	
			setElements(fornecedorSevice.pesquisar(getEntity()));
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_WARN, e.getMessage());
		}
	}
	
	public String maskCpfouCpnj(String mascara) {
		return StringUtil.maskCpfouCnpj(mascara);
	}
	
	@Override
	protected void beforeSearch() {
		if(getEstadoViewBean().getSearch()) {
			setElements(fornecedorSevice.todos());
		}
		setEntity(new Fornecedor());
	}
	
	public ETipoPessoa[] getTiposPessoa() {
		return ETipoPessoa.values();
	}

	public List<Fornecedor> getFornecedores() {
		return fornecedores;
	}

	public void setFornecedores(List<Fornecedor> fornecedores) {
		this.fornecedores = fornecedores;
	}

	public Fornecedor getFornecedorSelecionado() {
		return fornecedorSelecionado;
	}

	public void setFornecedorSelecionado(Fornecedor fornecedorSelecionado) {
		this.fornecedorSelecionado = fornecedorSelecionado;
	}

	public List<Fornecedor> getFornecedoresSelecionados() {
		return fornecedoresSelecionados;
	}

	public void setFornecedoresSelecionados(List<Fornecedor> fornecedoresSelecionados) {
		this.fornecedoresSelecionados = fornecedoresSelecionados;
	}
}
