package br.com.siscomanda.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.siscomanda.base.model.BaseEntity;

@Entity
@Table(name = "tamanho")
public class Tamanho extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 8703949887851128536L;
	
	@Column(name = "descricao", nullable = false)
	private String descricao;
	
	@Column(name = "sigla", nullable = false)
	private String sigla;
	
	@Column(name = "permite_meio_a_meio", nullable = false)
	private boolean permiteMeioAmeio;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tipo_id", nullable = false)
	private Tipo tipo;

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

	public boolean isPermiteMeioAmeio() {
		return permiteMeioAmeio;
	}

	public void setPermiteMeioAmeio(boolean permiteMeioAmeio) {
		this.permiteMeioAmeio = permiteMeioAmeio;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}
}
