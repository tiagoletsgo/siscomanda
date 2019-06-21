package br.com.siscomanda.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.siscomanda.base.model.BaseEntity;

public class ItemVenda extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 3248279660381728704L;

	private Venda venda;
	private Produto produto;
	private Double valor;
	private Double total;
	private Double quantidade;
	private String observacao;
	private List<Adicional> adicionais = new ArrayList<Adicional>();

	public ItemVenda() {
		this.valor = new Double(0);
		this.total = new Double(0);
		this.quantidade = new Double(1);
	}

	public ItemVenda(Venda venda, Produto produto, Double valor, Double quantidade, String observacao) {
		super();
		this.venda = venda;
		this.produto = produto;
		this.valor = valor;
		this.quantidade = quantidade;
		this.total = (valor * quantidade);
		this.observacao = observacao;
	}
	
	public ItemVenda(Long id, Venda venda, Produto produto, Double valor, Double quantidade, String observacao, List<Adicional> complementos) {
		super();
		setId(id);
		this.venda = venda;
		this.produto = produto;
		this.valor = valor;
		this.quantidade = quantidade;
		this.total = (valor * quantidade);
		this.observacao = observacao;
		this.adicionais = complementos;
	}
	
	public ItemVenda clonar(ItemVenda item) {
		ItemVenda it = new ItemVenda();
		it.setId(item.getId());
		it.setAdicionais(item.getAdicionais());
		it.setObservacao(item.getObservacao());
		it.setProduto(item.getProduto().clone(item.getProduto()));
		it.setQuantidade(item.getQuantidade());
		it.setSelecionado(item.isSelecionado());
		it.setTotal(item.getTotal());
		it.setValor(item.getValor());
		it.setVenda(item.getVenda());
		
		return it;
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
}
