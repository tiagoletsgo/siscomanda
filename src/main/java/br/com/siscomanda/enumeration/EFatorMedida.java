package br.com.siscomanda.enumeration;

public enum EFatorMedida {
	
	UN("UN"),
	KG("KG"),
	LT("LT");
	
	private String descricao;
	
	private EFatorMedida(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
