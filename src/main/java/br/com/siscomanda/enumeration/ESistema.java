package br.com.siscomanda.enumeration;

public enum ESistema {
	
	MESA("Mesa"),
	COMANDA("Comanda");
	
	private String descricao;
	
	private ESistema(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
