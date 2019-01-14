package br.com.napule.enumeration;

public enum ETipoPessoa {
	
	FISICA("Física"),
	JURIDICA("Jurídica");
	
	private String descricao;
	
	private ETipoPessoa(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
