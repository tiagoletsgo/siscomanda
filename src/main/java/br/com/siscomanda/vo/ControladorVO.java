package br.com.siscomanda.vo;

import java.io.Serializable;

import br.com.siscomanda.model.Venda;

public class ControladorVO implements Serializable {

	private static final long serialVersionUID = 1158024548588026835L;

	private int numero;
	private boolean disponivel;
	private Venda venda;

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public boolean isDisponivel() {
		return disponivel;
	}

	public void setDisponivel(boolean disponivel) {
		this.disponivel = disponivel;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}
}
