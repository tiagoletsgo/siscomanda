package br.com.siscomanda.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.siscomanda.base.model.BaseEntity;

@Entity
@Table(name = "tipo")
public class Tipo extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6842211770115826956L;
	
	@Column(name = "descricao", nullable = false)
	private String descricao;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
