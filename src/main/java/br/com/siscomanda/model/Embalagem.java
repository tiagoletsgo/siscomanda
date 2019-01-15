package br.com.siscomanda.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.siscomanda.base.model.BaseEntity;

@Entity
@Table(name = "embalagem")
public class Embalagem extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1040042679769035638L;
	
	@Column(name = "descricao", nullable = false)
	private String descricao;
	
	@Column(name = "sigla", nullable = false)
	private String sigla;
	
	@Column(name = "quantidade", nullable = false)
	private Integer quantidade;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
}
