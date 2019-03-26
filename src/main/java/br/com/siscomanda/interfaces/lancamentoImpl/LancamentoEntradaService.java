package br.com.siscomanda.interfaces.lancamentoImpl;

import java.io.Serializable;

import br.com.siscomanda.enumeration.ETipoOperacao;
import br.com.siscomanda.interfaces.CalculaLancamento;
import br.com.siscomanda.model.CaixaLancamento;

public class LancamentoEntradaService implements CalculaLancamento, Serializable {

	private static final long serialVersionUID = 8625336804329754661L;

	@Override
	public CaixaLancamento executaCalculo(CaixaLancamento lancamento, ETipoOperacao tipoOperacao, Double valor) {
		
		if(tipoOperacao.equals(ETipoOperacao.ENTRADA)) {
			lancamento.setValorEntrada(valor);
			lancamento.setValorSaida(new Double(0));
		}
		
		return lancamento;
	}

}
