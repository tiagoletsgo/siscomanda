package br.com.siscomanda.service;

import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.interfaces.Calculadora;

public class AplicaDescontoService implements Calculadora {

	@Override
	public Double aplicaCalculo(Double total, Double desconto, Double acrescimo) throws SiscomandaException {
		if(total == null || desconto == null || acrescimo == null) {
			throw new SiscomandaException("Não é permitido valores nulos");
		}
		
		return (total - desconto) + acrescimo;
	}

}
