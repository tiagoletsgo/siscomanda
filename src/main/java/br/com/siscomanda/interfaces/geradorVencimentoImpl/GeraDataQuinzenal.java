package br.com.siscomanda.interfaces.geradorVencimentoImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.siscomanda.enumeration.EFreaquencia;
import br.com.siscomanda.interfaces.GerarVencimento;

public class GeraDataQuinzenal extends GeradorData implements GerarVencimento {

	private static final long serialVersionUID = 4171684058981488242L;

	@Override
	public List<Date> frequencia(Date data, int quantidadeRepeticao) {
		List<Date> vencimento = new ArrayList<>();
		for(int i = 0; i < quantidadeRepeticao; i++) {
			data = executarVencimento(data, EFreaquencia.QUINZENAL);
			vencimento.add(data);
		}
		return vencimento;
	}
}
