package br.com.siscomanda.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.siscomanda.base.model.BaseEntity;

@Entity
@Table(name = "item_venda_adicional")
public class ItemVendaAdicional extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 4175750690428927319L;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "adicional_id", nullable = false)
	private Adicional adicional;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "produto_id", nullable = false)
	private Produto produto;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "venda_id", nullable = false)
	private Venda venda;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_venda_id", nullable = false)
	private ItemVenda itemVenda;

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

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public ItemVenda getItemVenda() {
		return itemVenda;
	}

	public void setItemVenda(ItemVenda itemVenda) {
		this.itemVenda = itemVenda;
	}
}
