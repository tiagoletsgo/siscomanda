package br.com.siscomanda.interfaces.lancamentoImpl;

import java.io.Serializable;

import br.com.siscomanda.enumeration.ETipoOperacao;
import br.com.siscomanda.interfaces.CalculaLancamento;
import br.com.siscomanda.model.Lancamento;

public class LancamentoSaida implements Serializable, CalculaLancamento {
	
	private static final long serialVersionUID = -401282639514056568L;

	@Override
	public Lancamento executaCalculo(Lancamento lancamento, ETipoOperacao tipoOperacao, Double valor) {
		
		if(tipoOperacao.equals(ETipoOperacao.SAIDA)) {
			lancamento.setDescricao("SAÍDA " + lancamento.getDescricao());
			lancamento.setValorSaida(valor);
			lancamento.setValorEntrada(new Double(0));
		}
		
		return lancamento;
	}
}
