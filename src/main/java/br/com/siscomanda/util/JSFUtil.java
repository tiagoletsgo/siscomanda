package br.com.siscomanda.util;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import br.com.siscomanda.exception.SiscomandaException;

public class JSFUtil implements Serializable {
	
	private static final long serialVersionUID = -7922117015682902028L;

	public static void addMessage(Severity tipo, String message) {
		getContext().addMessage(null, new FacesMessage(tipo, message, message));
	}
	
	public static String applicationPath() {
		return getContext().getExternalContext().getApplicationContextPath();
	}
	
	public static void redirect(String url) throws SiscomandaException {
		try {
			getContext().getExternalContext().redirect(url);
		} catch (IOException e) {
			throw new SiscomandaException(e.getMessage());
		}
	}
	
	public static HttpServletRequest getRequest() {
		return (HttpServletRequest)getContext().getExternalContext().getRequest();
	}
	
	public static FacesContext getContext() {
		return FacesContext.getCurrentInstance();
	}
}
