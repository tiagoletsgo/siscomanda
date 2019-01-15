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
	private int quantidade;
	
//	@Column(name = "subtotal", nullable = false)
	private double subtotal;

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
	
	@Override
	public int compareTo(ItemVenda o) {
		if(this.getId() < o.getId()) {
			return -1;
		}
		return 0;
	}
}
