package br.com.napule.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.napule.base.model.BaseEntity;

@Entity
@Table(name = "forma_pagamento")
public class FormaPagamento extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1785690096374815951L;
	
	@Column(name = "descricao", nullable = false, unique = true)
	private String descricao;
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
