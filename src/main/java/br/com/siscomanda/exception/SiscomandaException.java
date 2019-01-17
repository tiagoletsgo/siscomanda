package br.com.siscomanda.exception;

import java.io.Serializable;

public class SiscomandaException extends Exception implements Serializable {
	
	private static final long serialVersionUID = 2571272119070242670L;

	public SiscomandaException(String message) {
		super(message);
	}
	
	public SiscomandaException(String message, Throwable e) {
		super(message, e);
	}
}
