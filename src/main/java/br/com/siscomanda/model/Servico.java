package br.com.siscomanda.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.siscomanda.base.model.BaseEntity;

@Entity
@Table(name = "servico")
public class Servico extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1040042679769035638L;
	
	@Column(name = "descricao", nullable = false)
	private String descricao;
	
	@Column(name = "valor", nullable = false)
	private Double valor;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}
}
