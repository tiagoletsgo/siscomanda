package br.com.siscomanda.util;

import java.util.List;

import br.com.siscomanda.exception.SiscomandaException;

public class StringUtil {
	
	private static String simboloReal = "R$";
	
	public static String parseDouble(Double valor) {
		return simboloReal.concat(" ").concat(String.format("%.2f", valor));
	}
	
	public static String converterDouble(Double valor) {
		if(valor == null) {
			return new Double(0).toString();
		}
		
		if(valor.toString().contains(".0")) {
			return valor.toString().replace(".0", "");
		}
		return valor.toString();
	}
	
	public static void maisDeUmCampoPreenchido(List<String> campos) throws SiscomandaException {
		Integer selecionado = 0;
		for(String s : campos) {
			if(isNotEmpty(s)) {
				selecionado++;
			}
		}
		
		if(selecionado > 1) {
			throw new SiscomandaException("Não é permitido pesquisar com multiplos campos preenchidos.");
		}
	}
	
	public static boolean isEmpty(String value) {
		if(value == null || value.isEmpty()) {
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
