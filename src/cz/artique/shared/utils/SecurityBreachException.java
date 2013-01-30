package cz.artique.shared.utils;

public class SecurityBreachException extends ArtiqueException {
	private static final long serialVersionUID = 1L;

	public SecurityBreachException() {
		super("security breach detected");
	}

	public SecurityBreachException(String message) {
		super(message);
	}
}
