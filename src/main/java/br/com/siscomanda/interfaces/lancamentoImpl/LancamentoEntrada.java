package br.com.siscomanda.interfaces.lancamentoImpl;

import java.io.Serializable;

import br.com.siscomanda.enumeration.ETipoOperacao;
import br.com.siscomanda.interfaces.CalculaLancamento;
import br.com.siscomanda.model.Lancamento;

public class LancamentoEntrada implements CalculaLancamento, Serializable {

	private static final long serialVersionUID = 8625336804329754661L;

	@Override
	public Lancamento executaCalculo(Lancamento lancamento, ETipoOperacao tipoOperacao, Double valor) {
		
		if(tipoOperacao.equals(ETipoOperacao.ENTRADA)) {
			lancamento.setDescricao("ENTRADA " + lancamento.getDescricao());
			lancamento.setValorEntrada(valor);
			lancamento.setValorSaida(new Double(0));
		}
		
		return lancamento;
	}

}
