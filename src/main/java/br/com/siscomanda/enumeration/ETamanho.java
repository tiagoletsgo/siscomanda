package br.com.siscomanda.enumeration;

public enum ETamanho {
	
	PEQUENA("PEQUENA"),
	MEDIA("MÉDIA"),
	GRANDE("GRANDE");
	
	private String descricao;
	
	private ETamanho(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
