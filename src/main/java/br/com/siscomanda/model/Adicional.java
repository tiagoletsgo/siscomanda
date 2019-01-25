package br.com.siscomanda.model;

import java.io.Serializable;
import java.util.List;

import br.com.siscomanda.base.model.BaseEntity;
import br.com.siscomanda.enumeration.EFatorMedida;

public class Adicional extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 5617295055913668506L;
	
	private String descricao;
	
	private List<Categoria> categorias;
	
	private Double precoVenda;
	
	private Double precoCusto;
	
	private Boolean controlaEstoque;
	
	private EFatorMedida fatorMedida;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

	public Double getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(Double precoVenda) {
		this.precoVenda = precoVenda;
	}

	public Double getPrecoCusto() {
		return precoCusto;
	}

	public void setPrecoCusto(Double precoCusto) {
		this.precoCusto = precoCusto;
	}

	public Boolean getControlaEstoque() {
		return controlaEstoque;
	}

	public void setControlaEstoque(Boolean controlaEstoque) {
		this.controlaEstoque = controlaEstoque;
	}

	public EFatorMedida getFatorMedida() {
		return fatorMedida;
	}

	public void setFatorMedida(EFatorMedida fatorMedida) {
		this.fatorMedida = fatorMedida;
	}
}
