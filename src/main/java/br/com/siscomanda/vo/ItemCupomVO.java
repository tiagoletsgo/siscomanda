package br.com.siscomanda.vo;

import java.io.Serializable;

public class ItemCupomVO implements Serializable {

	private static final long serialVersionUID = -8787129012429601857L;
	private String descricao;
	private double quantidade;
	private double preco;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(double quantidade) {
		this.quantidade = quantidade;
	}

	public double getPreco() {
		return preco;
	}

	public void setPreco(double preco) {
		this.preco = preco;
	}
}
