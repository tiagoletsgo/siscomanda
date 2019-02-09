package br.com.siscomanda.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import br.com.siscomanda.base.model.BaseEntity;
import br.com.siscomanda.enumeration.EFatorMedida;

@Entity
@Table(name = "adicional")
public class Adicional extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 5617295055913668506L;
	
	@Column(name = "descricao", nullable = false)
	private String descricao;
	
	@Column(name = "preco_venda", nullable = false)
	private Double precoVenda;
	
	@Column(name = "preco_custo", nullable = false)
	private Double precoCusto;
	
	@Column(name = "controla_estoque", nullable = false)
	private Boolean controlaEstoque;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "fator_medida", nullable = false)
	private EFatorMedida fatorMedida;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "adicional_categoria", joinColumns = @JoinColumn(name = "adicional_id"), inverseJoinColumns = @JoinColumn(name = "categoria_id"))
	private List<Categoria> categorias;

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
