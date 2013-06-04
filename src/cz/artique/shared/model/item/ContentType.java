package cz.artique.shared.model.item;

public enum ContentType {
	PLAIN_TEXT("text/plain"),
	HTML("text/html");

	private static final String HTML_REGEX = "(?i)html";
	private static final String PLAIN_REGEX = "(?i)plain";

	private final String type;

	private ContentType(String type) {
		this.type = type;
	}

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

	public String getType() {
		return type;
	}
}
