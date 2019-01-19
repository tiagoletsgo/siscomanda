package br.com.siscomanda.model;

import java.io.Serializable;

import br.com.siscomanda.base.model.BaseEntity;

//@Entity
//@Table(name = "item_venda")
public class ItemVenda extends BaseEntity implements Serializable, Comparable<ItemVenda> {

	private static final long serialVersionUID = 7754528961077613833L;
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "produto_id", nullable = false)
	private Produto produto;
	
//	@Column(name = "quantidade", nullable = false)
	private Integer quantidade;
	
//	@Column(name = "subtotal", nullable = false)
	private Double subtotal;
	
//	@Column(name = "preco_venda", nullable = false)
	private Double precoVenda;

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}
	
	public Double getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(Double precoVenda) {
		this.precoVenda = precoVenda;
	}

	@Override
	public int compareTo(ItemVenda o) {
		if(this.getId() < o.getId()) {
			return -1;
		}
		return 0;
	}
}
