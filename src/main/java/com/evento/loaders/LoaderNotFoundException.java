package com.evento.loaders;

/**
 * Excessao usada para representar falhas genericas no acesso aos dados
 */
public class LoaderNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 3618133446711523129L;

	public LoaderNotFoundException() {
	}

	public LoaderNotFoundException(String message) {
		super(message);
	}

	public LoaderNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoaderNotFoundException(Throwable cause) {
		super(cause);
	}
}