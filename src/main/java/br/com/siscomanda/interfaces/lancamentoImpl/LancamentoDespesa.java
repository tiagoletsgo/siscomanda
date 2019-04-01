package br.com.siscomanda.interfaces.lancamentoImpl;

import java.io.Serializable;

import br.com.siscomanda.enumeration.ETipoOperacao;
import br.com.siscomanda.interfaces.CalculaLancamento;
import br.com.siscomanda.model.CaixaLancamento;

public class LancamentoDespesa implements CalculaLancamento, Serializable {

	private static final long serialVersionUID = 8625336804329754661L;
	
	@Override
	public CaixaLancamento executaCalculo(CaixaLancamento lancamento, ETipoOperacao tipoOperacao, Double valor) {
		
		if(tipoOperacao.equals(ETipoOperacao.DESPESA)) {
			lancamento.setDescricao("SAÍDA DESPESA " + lancamento.getDescricao());
			lancamento.setValorEntrada(new Double(0));
			lancamento.setValorSaida(valor);
		}
		
		return lancamento;
	}

}
