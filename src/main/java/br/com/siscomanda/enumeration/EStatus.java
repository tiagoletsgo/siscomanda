package br.com.siscomanda.enumeration;

public enum EStatus {
	
	INICIADO("Iniciado"),
	AGUARDANDO("Aguardando"),
	OCUPADO("Ocupado"),
	LIVRE("Livre"),
	PENDENTE("Pendente"),
	SAIU_PARA_ENTREGA("Saiu p/Entrega"),
	FINALIZADO_ENTREGUE("Finalizado/Entregue"),
	EM_ABERTO("Em aberto");
	
	private String descricao;
	
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
