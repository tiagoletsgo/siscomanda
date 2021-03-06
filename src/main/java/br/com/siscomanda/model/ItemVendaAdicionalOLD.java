package br.com.siscomanda.model;

import java.io.Serializable;

import br.com.siscomanda.base.model.BaseEntity;

//@Entity
//@Table(name = "item_venda_adicional")
public class ItemVendaAdicionalOLD extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 4175750690428927319L;
	
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "adicional_id", nullable = false)
	private Adicional adicional;
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "produto_id", nullable = false)
	private Produto produto;
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "venda_id", nullable = false)
	private VendaOLD venda;
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "item_venda_id", nullable = false)
	private ItemVendaOLD itemVenda;

	public Adicional getAdicional() {
		return adicional;
	}

	public void setAdicional(Adicional adicional) {
		this.adicional = adicional;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public VendaOLD getVenda() {
		return venda;
	}

	public void setVenda(VendaOLD venda) {
		this.venda = venda;
	}

	public ItemVendaOLD getItemVenda() {
		return itemVenda;
	}

	public void setItemVenda(ItemVendaOLD itemVenda) {
		this.itemVenda = itemVenda;
	}
}
