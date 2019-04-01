package br.com.siscomanda.enumeration;

public enum EFreaquencia {
	
	DIARIO("Diário"),
	SEMANAL("Semanal"),
	QUINZENAL("Quinzenal"),
	MENSAL("Mensal"),
	ANUAL("Anual");
	
	private String descricao;

	private EFreaquencia(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
