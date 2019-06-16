package br.com.siscomanda.util;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

public class JSFUtil implements Serializable {
	
	private static final long serialVersionUID = -7922117015682902028L;

	public static void addMessage(Severity tipo, String message) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(tipo, message, message));
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
}
