package br.com.siscomanda.interfaces;

import java.util.Date;
import java.util.List;

public interface GerarVencimento {
	
	public List<Date> frequencia(Date data, int quantidadeRepeticao);
}
