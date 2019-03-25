package br.com.siscomanda.interfaces;

import br.com.siscomanda.exception.SiscomandaException;

public interface Calculadora {
	
	public Double aplicaCalculo(Double total, Double desconto, Double acrescimo) throws SiscomandaException;
}
