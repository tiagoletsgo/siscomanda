package br.com.siscomanda.enumeration;

public enum ETipoOperacao {
	
	SAIDA_DESPESA("Saída - Lançamento de despesas"),
	SAIDA_SANGRIA("Saída - Sangria"),
	ENTRADA_ACRESCIMO("Entrada - Acrescímo");
	
	private String descricao;
	
	private ETipoOperacao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
