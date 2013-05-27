package cz.artique.shared.model.item;

public enum ContentType {
	PLAIN_TEXT("text/plain"),
	HTML("text/html");

	private final String type;

	private ContentType(String type) {
		this.type = type;
	}

	private static final String HTML_REGEX = "(?i)html";

	public static ContentType guess(String name) {
		if (name.matches(HTML_REGEX)) {
			return HTML;
		}
		return PLAIN_TEXT;
	}

	public String getType() {
		return type;
	}
}
