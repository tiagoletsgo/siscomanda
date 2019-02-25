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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

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
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	@JoinTable(name = "adicional_categoria", joinColumns = @JoinColumn(name = "adicional_id"), inverseJoinColumns = @JoinColumn(name = "categoria_id"))
	private List<Categoria> categorias;
	
	@Transient
	private String descricaoCategoria;
	
	@Transient
	private Produto produto;
	
	public Adicional() {	}
	
	public Adicional(Long id) {
		setId(id);
	}

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

	public String getDescricaoCategoria() {
		return descricaoCategoria;
	}

	public void setDescricaoCategoria(String descricaoCategoria) {
		this.descricaoCategoria = descricaoCategoria;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}
}
