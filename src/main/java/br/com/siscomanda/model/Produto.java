package br.com.siscomanda.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	
	@Column(name = "preco_custo", nullable = false)
	private Double precoCusto = BigDecimal.ZERO.doubleValue();
	
	@Column(name = "preco_venda", nullable = false)
	private Double precoVenda = BigDecimal.ZERO.doubleValue();
	
	@Column(name = "estoque_minimo")
	private Double estoqueMinimo = BigDecimal.ZERO.doubleValue();
	
	@Column(name = "estoque_maximo")
	private Double estoqueMaximo = BigDecimal.ZERO.doubleValue();
	
	@Column(name = "permite_meio_a_meio", nullable = false)
	private boolean permiteMeioAmeio;

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

	public Double getPrecoCusto() {
		return precoCusto;
	}

	public void setPrecoCusto(Double precoCusto) {
		this.precoCusto = precoCusto;
	}

	public Double getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(Double precoVenda) {
		this.precoVenda = precoVenda;
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

	public boolean isPermiteMeioAmeio() {
		return permiteMeioAmeio;
	}

	public void setPermiteMeioAmeio(boolean permiteMeioAmeio) {
		this.permiteMeioAmeio = permiteMeioAmeio;
	}
}
