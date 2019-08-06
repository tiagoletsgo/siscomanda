package br.com.siscomanda.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.siscomanda.base.model.BaseEntity;

@Entity
@Table(name = "item_venda")
public class ItemVenda extends BaseEntity implements Serializable, Comparable<ItemVenda> {

	private static final long serialVersionUID = 3248279660381728704L;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "venda_id", nullable = false)
	private Venda venda;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "produto_id", nullable = false)
	private Produto produto;
	
	@Column(name = "valor", nullable = false)
	private Double valor;
	
	@Column(name = "total", nullable = false)
	private Double total;
	
	@Column(name = "quantidade", nullable = false)
	private Double quantidade;
	
	@Column(name = "observacao", nullable = false)
	private String observacao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tamanho_id", nullable = false)
	private Tamanho tamanho;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "item_adicional", joinColumns = @JoinColumn(name = "item_venda_id", unique = false), inverseJoinColumns = @JoinColumn(name = "adicional_id", unique = false))
	private List<Adicional> adicionais = new ArrayList<Adicional>();

	public ItemVenda() {
		this.valor = new Double(0);
		this.total = new Double(0);
		this.quantidade = new Double(1);
	}

	public ItemVenda(Venda venda, Produto produto, Double valor, Double quantidade, String observacao) {
		this.venda = venda;
		this.produto = produto;
		this.valor = valor;
		this.quantidade = quantidade;
		this.total = (valor * quantidade);
		this.observacao = observacao;
	}
	
	public ItemVenda(Produto produto, Double valor, Double quantidade, String observacao) {
		this.produto = produto;
		this.valor = valor;
		this.quantidade = quantidade;
		this.total = (valor * quantidade);
		this.observacao = observacao;
	}
	
	public ItemVenda(Long id, Venda venda, Produto produto, Double valor, Double quantidade, String observacao, List<Adicional> complementos) {
		setId(id);
		this.venda = venda;
		this.produto = produto;
		this.valor = valor;
		this.quantidade = quantidade;
		this.total = (valor * quantidade);
		this.observacao = observacao;
		this.adicionais = complementos;
	}
	
	public ItemVenda clone(ItemVenda item) {
		ItemVenda itemm = new ItemVenda();
		itemm.setId(item.getId());
		
		List<Adicional> adicionais = new ArrayList<Adicional>();
		adicionais.addAll(item.getAdicionais());
		itemm.setAdicionais(adicionais);
		
		itemm.setObservacao(item.getObservacao());
		itemm.setProduto(item.getProduto().clone(item.getProduto()));
		itemm.setQuantidade(item.getQuantidade());
		itemm.setSelecionado(item.isSelecionado());
		itemm.setTotal(item.getTotal());
		itemm.setValor(item.getValor());
		itemm.setVenda(item.getVenda());
		itemm.setTamanho(item.getTamanho());
		
		return itemm;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public List<Adicional> getAdicionais() {
		return adicionais;
	}

	public void setAdicionais(List<Adicional> adicionais) {
		this.adicionais = adicionais;
	}

	public Tamanho getTamanho() {
		return tamanho;
	}

	public void setTamanho(Tamanho tamanho) {
		this.tamanho = tamanho;
	}

	@Override
	public int compareTo(ItemVenda o) {
		if(o.getId() > getId()) {
			return -1;
		}
		return 0;
	}
}
