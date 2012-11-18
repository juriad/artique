package cz.artique.shared.utils;

import java.io.Serializable;

/**
 * Property value is invalid
 */
public class PropertyValueException extends ArtiqueException
		implements Serializable {

	private static final long serialVersionUID = 1L;
	private final String property;

	public PropertyValueException(String property) {
		super();
		this.property = property;
	}

	public String getProperty() {
		return property;
	}
}
