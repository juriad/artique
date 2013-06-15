package cz.artique.shared.model.item;

/**
 * Represents MIME type of content of {@link Item} or
 * {@link PageChangeItem#getDiff()} of {@link PageChangeItem}.
 * 
 * @author Adam Juraszek
 * 
 */
public enum ContentType {
	/**
	 * Represents text/plain value.
	 */
	PLAIN_TEXT("text/plain"),
	/**
	 * Represents text/html value.
	 */
	HTML("text/html");

	private static final String HTML_REGEX = "(?i)html";
	private static final String PLAIN_REGEX = "(?i)plain";

	private final String type;

	private ContentType(String type) {
		this.type = type;
	}

	/**
	 * Guess the content type first by its name, secondly by presence of
	 * &lt;&gt; characters in content.
	 * 
	 * @param name
	 * @param content
	 * @return
	 */
	public static ContentType guess(String name, String content) {
		if (name != null) {
			if (name.matches(HTML_REGEX)) {
				return HTML;
			}
			if (name.matches(PLAIN_REGEX)) {
				return PLAIN_TEXT;
			}
		}
		if (content.contains("<") && content.contains(">")) {
			return HTML;
		} else {
			return PLAIN_TEXT;
		}
	}

	/**
	 * @return MIME representation of value of this content type
	 */
	public String getType() {
		return type;
	}
}
