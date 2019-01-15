package br.com.siscomanda.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import br.com.siscomanda.base.model.BaseEntity;
import br.com.siscomanda.enumeration.ESistema;

@Entity
@Table(name = "definicao_geral")
public class DefinicaoGeral extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 5565669778293412878L;
	
	@Column(name = "razao_social", nullable = false)
	private String razaoSocial;
	
	@Column(name = "nome_fantasia", nullable = false)
	private String nomeFantasia;
	
	@Column(nullable = false, unique = true)
	private String cnpj;
	
	@Column(name = "inscricao_estadual")
	private String inscricaoEstadual;
	
	@Column(name = "fone_principal")
	private String fonePrincipal;
	
	@Embedded
	private Endereco endereco;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "sistema")
	private ESistema sistema;
	
	@Column(name  = "quantidade_mesa_comanda")
	private int qtdMesaComanda;
	
	@Column(name = "taxa_servico")
	private Double taxaServico = BigDecimal.ZERO.doubleValue();
	
	public DefinicaoGeral() {
		setEndereco(new Endereco());
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	public String getFonePrincipal() {
		return fonePrincipal;
	}

	public void setFonePrincipal(String fonePrincipal) {
		this.fonePrincipal = fonePrincipal;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public ESistema getSistema() {
		return sistema;
	}

	public void setSistema(ESistema sistema) {
		this.sistema = sistema;
	}

	public int getQtdMesaComanda() {
		return qtdMesaComanda;
	}

	public void setQtdMesaComanda(int qtdMesaComanda) {
		this.qtdMesaComanda = qtdMesaComanda;
	}

	public Double getTaxaServico() {
		return taxaServico;
	}

	public void setTaxaServico(Double taxaServico) {
		this.taxaServico = taxaServico;
	}
}
