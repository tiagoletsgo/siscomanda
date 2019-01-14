package br.com.napule.config.jsf;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

public class JSFUtil implements Serializable {
	
	private static final long serialVersionUID = -7922117015682902028L;

	public static void addMessage(Severity tipo, String message) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(tipo, message, message));
	}
}
