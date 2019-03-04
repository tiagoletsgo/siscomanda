package br.com.siscomanda.enumeration;

public enum EStatus {
	
	EM_ABERTO("Em aberto"),
	CANCELADO("Cancelado"),
	PAGO("Pago"),
	PAGO_PARCIAL("Pago parcial");
	
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
