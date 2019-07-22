package br.com.siscomanda.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import br.com.siscomanda.exception.SiscomandaException;

public class StringUtil {
	
	private static String simboloReal = "R$";
		
	public static String parseDouble(Double valor) {
		return simboloReal.concat(" ").concat(String.format("%.2f", valor));
	}
	
	public static String converterValorFracionado(Double valor) {
		String valorConvertido = "1/0";
		
		if(valor.toString().startsWith("0")) {
			int count = 0;
			double valorCalculado = 0.0;
			
			while(valorCalculado != valor) {
				valorCalculado = (new Double(1) / new Double(count));
				count++;
			}
			
			valorConvertido = valorConvertido.replace("0", new Integer(count - 1).toString());
		}
		else {
			DecimalFormat formater = new DecimalFormat("#,##0.00");
			formater.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale("pt", "BR")));
			valorConvertido = valor.toString();
			valorConvertido = formater.format(new Double(valorConvertido));
		}
		
		return valorConvertido;
	}
	
	public static String converterParaValorMonetario(Double valor) {
		StringBuilder valorFormatado = new StringBuilder();
		DecimalFormat f = new DecimalFormat("#,##0.00");
		f.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale("pt", "BR")));
		
		valorFormatado.append(f.getDecimalFormatSymbols().getCurrencySymbol());
		valorFormatado.append(" ");
		valorFormatado.append(f.format(valor));
		
		return valorFormatado.toString();
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
	
	/**
	 * <p>Metodo para complementar uma string com algum caracter a esquerda. Se o valor for maior 
	 * que o pad um corte será realizado ate que o valor fique do tamanho do pad.
	 * </p>
	 * @param valor
	 * @param pad - Com quantos caracteres a esquerda, por exemplo: 10
	 * @param caracter - Com qual caracter a ser complementado por exemplo: 0 ou A
	 * @return 
	 */
	public static String leftPad(String valor, int pad, String caracter) {
		int tamanho = valor.length();
		int soma = pad - tamanho;
		int count = 0;
		
		if(soma < 0) {
			while(soma < 0) {
				valor = valor.substring(1);
				tamanho = valor.length();
				soma = pad - tamanho;
			}
		}
		
		StringBuilder padding = new StringBuilder();
		while(count < soma) {
			padding.append(caracter);
			count++;
		}
		
		return padding.append(valor).toString();
	}
	
	public static String maskCelular(String numeroCelular) {
		StringBuilder mask = new StringBuilder();
		mask.append("(");
		mask.append(numeroCelular.substring(0, 2));
		mask.append(") ");
		mask.append(numeroCelular.substring(2, 3));
		mask.append(" ");
		mask.append(numeroCelular.substring(3, 7));
		mask.append("-");
		mask.append(numeroCelular.substring(7, 11));
		return mask.toString();
	}
	
	public static String maskCpfouCnpj(String mascara) {
		StringBuilder mask = new StringBuilder();

		if(mascara.length() == 11) {
			mask.append(mascara.substring(0, 3)).append(".");
			mask.append(mascara.substring(3, 6)).append(".");
			mask.append(mascara.substring(6, 9)).append("-");
			mask.append(mascara.substring(9, 11));
		}
		else if(mascara.length() == 14) {
			mask.append(mascara.substring(0, 2)).append(".");
			mask.append(mascara.substring(2, 5)).append(".");
			mask.append(mascara.substring(5, 8)).append("/");
			mask.append(mascara.substring(8, 12)).append("-");
			mask.append(mascara.substring(12, 14));
		}
		else {
			mask.append(mascara);
		}
		
		return mask.toString();
	}
	
	public static String addValorVazio() {
		return "";
	}
}
