package br.com.siscomanda.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import br.com.siscomanda.base.model.BaseEntity;
import br.com.siscomanda.enumeration.ETipoPessoa;

@Entity
@Table(name = "fornecedor")
public class Fornecedor extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -2684167123656311227L;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_pessoa", nullable = false)
	private ETipoPessoa tipoPessoa;
	
	@Column(name = "razao_social", nullable = false)
	private String razaoSocial;
	
	@Column(name = "nome_fantasia", nullable = false)
	private String nomeFantasia;
	
	@Column(name = "cpf_cnpj", nullable = false, unique = true)
	private String cpfCnpj;
	
	@Embedded
	private Endereco endereco;
	
	@Column(name = "telefone_fixo")
	private String telefoneFixo;
	
	@Column(name = "telefone_celular", nullable = false)
	private String telefoneCelular;
	
	@Column(name = "email")
	private String email;
	
	public Fornecedor() {
		setEndereco(new Endereco());
	}

	public ETipoPessoa getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(ETipoPessoa tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
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

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public String getTelefoneFixo() {
		return telefoneFixo;
	}

	public void setTelefoneFixo(String telefoneFixo) {
		this.telefoneFixo = telefoneFixo;
	}

	public String getTelefoneCelular() {
		return telefoneCelular;
	}

	public void setTelefoneCelular(String telefoneCelular) {
		this.telefoneCelular = telefoneCelular;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
