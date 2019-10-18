package br.com.siscomanda.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.siscomanda.model.Cliente;

public class CupomCozinhaVO implements Serializable {

	private static final long serialVersionUID = -2264859120028210258L;

	private String titulo;
	private Long numeroVenda;
	private Long numeroOperacao;
	private String tipoVenda;
	private String operador;
	private Date dataHora;
	private Cliente cliente = new Cliente();
	private List<ItemCupomVO> itens = new ArrayList<>();

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Long getNumeroVenda() {
		return numeroVenda;
	}

	public void setNumeroVenda(Long numeroVenda) {
		this.numeroVenda = numeroVenda;
	}

	public Long getNumeroOperacao() {
		return numeroOperacao;
	}

	public void setNumeroOperacao(Long numeroOperacao) {
		this.numeroOperacao = numeroOperacao;
	}

	public String getTipoVenda() {
		return tipoVenda;
	}

	public void setTipoVenda(String tipoVenda) {
		this.tipoVenda = tipoVenda;
	}

	public String getOperador() {
		return operador;
	}

	public void setOperador(String operador) {
		this.operador = operador;
	}

	public Date getDataHora() {
		return dataHora;
	}

	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<ItemCupomVO> getItens() {
		return itens;
	}

	public void setItens(List<ItemCupomVO> itens) {
		this.itens = itens;
	}
}
