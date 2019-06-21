package br.com.siscomanda.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.siscomanda.base.model.BaseEntity;

@Entity
@Table(name = "produto")
public class Produto extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -6913743593156149859L;
	
	@Column(name = "descricao", nullable = false)
	private String descricao;
	
	@Column(name = "codigo_ean", nullable = false)
	private String codigoEan;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "embalagem_id", nullable = false)
	private Embalagem embalagem;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fornecedor_id", nullable = false)
	private Fornecedor fornecedor;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categoria_id", nullable = false)
	private Categoria categoria;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subcategoria_id", nullable = false)
	private SubCategoria subCategoria;
	
	@Column(name = "controla_estoque")
	private boolean controlaEstoque;
	
	@Column(name = "estoque_minimo")
	private Double estoqueMinimo = BigDecimal.ZERO.doubleValue();
	
	@Column(name = "estoque_maximo")
	private Double estoqueMaximo = BigDecimal.ZERO.doubleValue();
	
	@Column(name = "permite_adicional", nullable = false)
	private boolean permiteAdicional;
	
	@OneToMany(mappedBy = "produto", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Preco> precos = new ArrayList<Preco>();
	
	public Produto() {	}
	
	public Produto(Long id) {
		setId(id);
		
	}
	
	public Produto(Long id, String descricao) {
		setId(id);
		setDescricao(descricao);
	}
	
	public Produto clone(Produto produto) {
		Produto prod = new Produto();
		prod.setId(produto.getId());
		prod.setDescricao(produto.getDescricao());
		prod.setCodigoEan(produto.getCodigoEan());
		prod.setEmbalagem(produto.getEmbalagem());
		prod.setFornecedor(produto.getFornecedor());
		prod.setCategoria(produto.getCategoria());
		prod.setSubCategoria(produto.getSubCategoria());
		prod.setControlaEstoque(produto.getControlaEstoque());
		prod.setEstoqueMinimo(produto.getEstoqueMinimo());
		prod.setEstoqueMaximo(produto.getEstoqueMaximo());
		prod.setPermiteAdicional(produto.isPermiteAdicional());
		prod.setPrecos(produto.getPrecos());
		return prod;
	}
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getCodigoEan() {
		return codigoEan;
	}

	public void setCodigoEan(String codigoEan) {
		this.codigoEan = codigoEan;
	}

	public Embalagem getEmbalagem() {
		return embalagem;
	}

	public void setEmbalagem(Embalagem embalagem) {
		this.embalagem = embalagem;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public SubCategoria getSubCategoria() {
		return subCategoria;
	}

	public void setSubCategoria(SubCategoria subCategoria) {
		this.subCategoria = subCategoria;
	}

	public boolean getControlaEstoque() {
		return controlaEstoque;
	}

	public void setControlaEstoque(boolean controlaEstoque) {
		this.controlaEstoque = controlaEstoque;
	}

	public Double getEstoqueMinimo() {
		return estoqueMinimo;
	}

	public void setEstoqueMinimo(Double estoqueMinimo) {
		this.estoqueMinimo = estoqueMinimo;
	}

	public Double getEstoqueMaximo() {
		return estoqueMaximo;
	}

	public void setEstoqueMaximo(Double estoqueMaximo) {
		this.estoqueMaximo = estoqueMaximo;
	}

	public boolean isPermiteAdicional() {
		return permiteAdicional;
	}

	public void setPermiteAdicional(boolean permiteAdicional) {
		this.permiteAdicional = permiteAdicional;
	}

	public List<Preco> getPrecos() {
		return precos;
	}

	public void setPrecos(List<Preco> precos) {
		this.precos = precos;
	}
}
