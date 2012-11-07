package cz.artique.utils;

/**
 * TODO Auto-generated RuntimeException
 */
public class SourceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * @see RuntimeException#RuntimeException()
	 */
	public SourceException() {
		super();
	}

	/**
	 * @see RuntimeException#RuntimeException(String)
	 */
	public SourceException(String message) {
		super(message);
	}

	/**
	 * @see RuntimeException#RuntimeException(String, Throwable)
	 */
	public SourceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see RuntimeException#RuntimeException(Throwable)
	 */
	public SourceException(Throwable cause) {
		super(cause);
	}
}
