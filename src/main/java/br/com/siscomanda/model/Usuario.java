package br.com.siscomanda.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.siscomanda.base.model.BaseEntity;

@Entity
@Table(name = "usuario")
public class Usuario extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -4462542131483482913L;
	
	@Column(name = "nome", nullable = false)
	private String nome;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "login", nullable = false)
	private String login;
	
	@Column(name = "senha", nullable = false)
	private String senha;
	
	public Usuario() {	}
	
	public Usuario(String login) {
		this.login = login;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

}
