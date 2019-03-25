package br.com.siscomanda.interfaces;

import br.com.siscomanda.enumeration.ETipoOperacao;
import br.com.siscomanda.model.CaixaLancamento;

public interface CalculaLancamento {
	
	public CaixaLancamento executaCalculo(CaixaLancamento lancamento, ETipoOperacao tipoOperacao, Double valor);
}
