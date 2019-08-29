package br.com.siscomanda.enumeration;

public enum EStatus {
	
	EM_ABERTO("aberto"),
	CANCELADO("Cancelado"),
	PAGO_PARCIAL("Pago parcial"),
	PAGO("Pago");
	
	private String descricao;
	
	public static EStatus toStatus(String status) {
		switch(status.toUpperCase()) {
		case "EM_ABERTO" :
			return EM_ABERTO;
		case "CANCELADO" :
			return CANCELADO;
		case "PAGO_PARCIAL" :
			return PAGO_PARCIAL;
		case "PAGO" :
			return PAGO;
		default:
			return null;
		}
	}
	
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
