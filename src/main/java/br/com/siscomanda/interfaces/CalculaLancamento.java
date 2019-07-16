package br.com.siscomanda.interfaces;

import br.com.siscomanda.enumeration.ETipoOperacao;
import br.com.siscomanda.model.Lancamento;

public interface CalculaLancamento {
	
	public Lancamento executaCalculo(Lancamento lancamento, ETipoOperacao tipoOperacao, Double valor);
}
