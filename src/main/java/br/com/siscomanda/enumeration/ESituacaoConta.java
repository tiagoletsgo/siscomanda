package br.com.siscomanda.enumeration;

public enum ESituacaoConta {
	
	CONTA_A_PAGAR("Contas a Pagar"),
	CONTA_PAGA("Contas Pagas"),
	CONTA_VENCIDA("Contas Vencidas");
	
	private String descricao;
	
	private ESituacaoConta(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
