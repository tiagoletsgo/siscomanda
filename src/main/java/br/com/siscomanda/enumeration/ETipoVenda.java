package br.com.siscomanda.enumeration;

public enum ETipoVenda {
	
	MESA_COMANDA("Mesa / Comanda"),
	BALCAO("Balcão"),
	DELIVERY("Delivery");
	
	private String descricao;
	
	private ETipoVenda(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
