package br.com.siscomanda.enumeration;

public enum EStatus {
	
	EM_ABERTO("aberto"),
	CANCELADO("Cancelado"),
	PAGO_PARCIAL("Pago parcial"),
	PAGO("Pago");
	
	private String descricao;
	
	private EStatus(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
