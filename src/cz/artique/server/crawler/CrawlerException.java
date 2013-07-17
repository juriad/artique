package cz.artique.server.crawler;

/**
 * Exception of this type is thrown whenever Crawling fails.
 * 
 * @author Adam Juraszek
 * 
 */
public class CrawlerException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * @see RuntimeException#RuntimeException(String)
	 */
	public CrawlerException(String message) {
		super(message);
	}

	/**
	 * @see RuntimeException#RuntimeException(String, Throwable)
	 */
	public CrawlerException(String message, Throwable cause) {
		super(message, cause);
	}
}
