package br.com.siscomanda.exception;

public class SiscomandaRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 4870124632601341427L;
	
	public SiscomandaRuntimeException() {
	}
	
	public SiscomandaRuntimeException(String message) {
		super(message);
	}
	
	public SiscomandaRuntimeException(Throwable throwable, String message) {
		super(message, throwable);
	}
}
