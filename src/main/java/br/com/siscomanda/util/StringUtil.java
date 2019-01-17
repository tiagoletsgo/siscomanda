package br.com.siscomanda.util;

import java.util.List;

import br.com.siscomanda.exception.SiscomandaException;

public class StringUtil {
	
	public static void maisDeUmCampoPreenchido(List<String> campos) throws SiscomandaException {
		Integer selecionado = 0;
		for(String s : campos) {
			if(!s.isEmpty()) {
				selecionado++;
			}
		}
		
		if(selecionado > 1) {
			throw new SiscomandaException("Não é permitido pesquisar com multiplos campos preenchidos.");
		}
	}
	
	public static boolean isEmpty(String value) {
		if(value.isEmpty() || value == null) {
			return true;
		}
		return false;
	}
	
	public static boolean isNotEmpty(String value) {
		return !isEmpty(value);
	}
	
	public static String somenteAlfanumerico(String value) {
		return value.replaceAll("[^a-z A-Z 0-9]+", "").trim();
	}
	
	public static String somenteAlfanumericoSemEspaco(String value) {
		return removeEspacoEmBranco(value.replaceAll("[^a-z A-Z 0-9]+", ""));
	}
	
	public static String removeEspacoEmBranco(String value) {
		return value.replaceAll("\\s", "").trim();
	}
}
