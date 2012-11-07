package cz.artique.shared.model.item;

public enum ContentType {
	PLAIN_TEXT,
	HTML;

	private static final String HTML_REGEX = "(?i)html";

	public static ContentType guess(String name) {
		if (name.matches(HTML_REGEX)) {
			return HTML;
		}
		return PLAIN_TEXT;
	}
}
