package br.com.napule.enumeration;

public enum EControlaEstoque {
	
	SIM("SIM"),
	NAO("NÃO");
	
	private String descricao;
	
	private EControlaEstoque(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
