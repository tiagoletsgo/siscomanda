package br.com.napule.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.napule.base.model.BaseEntity;

@Entity
@Table(name = "bandeira")
public class Bandeira extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -1859718848494181111L;
	
	@Column(name = "descricao", nullable = false, unique = true)
	private String descricao;
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}