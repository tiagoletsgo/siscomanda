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
@Table(name = "preco")
public class Preco extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -629535351628526494L;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "produto_id", nullable = false)
	private Produto produto;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tamanho_id", nullable = false)
	private Tamanho tamanho;
	
	@Column(name = "preco_custo", nullable = false)
	private Double precoCusto = BigDecimal.ZERO.doubleValue();
	
	@Column(name = "preco_venda", nullable = false)
	private Double precoVenda = BigDecimal.ZERO.doubleValue();
	
	@Column(name = "disponibiliza_para_venda", nullable = false)
	private boolean disponibilizaParaVenda;

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
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

	public boolean isDisponibilizaParaVenda() {
		return disponibilizaParaVenda;
	}

	public void setDisponibilizaParaVenda(boolean disponibilizaParaVenda) {
		this.disponibilizaParaVenda = disponibilizaParaVenda;
	}

	public Tamanho getTamanho() {
		return tamanho;
	}

	public void setTamanho(Tamanho tamanho) {
		this.tamanho = tamanho;
	}
}
