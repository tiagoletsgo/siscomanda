package br.com.napule.exception;

import java.io.Serializable;

public class NapuleException extends Exception implements Serializable {
	
	private static final long serialVersionUID = 2571272119070242670L;

	public NapuleException(String message) {
		super(message);
	}
	
	public NapuleException(String message, Throwable e) {
		super(message, e);
	}
}
