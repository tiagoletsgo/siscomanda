package br.com.siscomanda.interfaces.geradorVencimentoImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.siscomanda.enumeration.EFreaquencia;
import br.com.siscomanda.interfaces.GerarVencimento;

public class GeraDataDiario extends GeradorData implements GerarVencimento {

	private static final long serialVersionUID = -7670008156710807529L;

	@Override
	public List<Date> frequencia(Date data, int quantidadeRepeticao) {
		List<Date> vencimento = new ArrayList<>();
		for(int i = 0; i < quantidadeRepeticao; i++) {
			data = executarVencimento(data, EFreaquencia.DIARIO);
			vencimento.add(data);
		}
		return vencimento;
	}
}
