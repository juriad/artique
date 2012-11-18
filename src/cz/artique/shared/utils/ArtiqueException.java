package cz.artique.shared.utils;

import java.io.Serializable;

/**
 * Common ancestor for all exceptions
 */
public class ArtiqueException extends RuntimeException implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * @see RuntimeException#RuntimeException()
	 */
	public ArtiqueException() {
		super();
	}

	/**
	 * @see RuntimeException#RuntimeException(String)
	 */
	public ArtiqueException(String message) {
		super(message);
	}

	/**
	 * @see RuntimeException#RuntimeException(String, Throwable)
	 */
	public ArtiqueException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see RuntimeException#RuntimeException(Throwable)
	 */
	public ArtiqueException(Throwable cause) {
		super(cause);
	}
}
