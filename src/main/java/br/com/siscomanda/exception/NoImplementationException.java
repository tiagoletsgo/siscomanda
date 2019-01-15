package br.com.siscomanda.exception;

public class NoImplementationException extends RuntimeException {

	private static final long serialVersionUID = 4870124632601341427L;
	
	public NoImplementationException() {
	}
	
	public NoImplementationException(String message) {
		super(message);
	}
	
	public NoImplementationException(Throwable throwable, String message) {
		super(message, throwable);
	}
}
