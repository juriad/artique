package cz.artique.server.crawler;

/**
 * TODO Auto-generated RuntimeException
 */
public class CrawlerException extends RuntimeException {
	/**
	 * TODO Auto-generated Default Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @see RuntimeException#RuntimeException()
	 */
	public CrawlerException() {
		super();
	}

	/**
	 * @see RuntimeException#RuntimeException(String)
	 */
	public CrawlerException(String message) {
		super(message);
	}

	/**
	 * @see RuntimeException#RuntimeException(Throwable)
	 */
	public CrawlerException(Throwable cause) {
		super(cause);
	}

	/**
	 * @see RuntimeException#RuntimeException(String, Throwable)
	 */
	public CrawlerException(String message, Throwable cause) {
		super(message, cause);
	}
}
