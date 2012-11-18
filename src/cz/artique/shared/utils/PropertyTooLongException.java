package cz.artique.shared.utils;

import java.io.Serializable;

/**
 * Common ancestor for all exceptions
 */
public class PropertyTooLongException extends PropertyValueException
		implements Serializable {

	private static final long serialVersionUID = 1L;

	private final int limit;
	private final String value;

	public PropertyTooLongException(String property, String value, int limit) {
		super(property);
		this.value = value;
		this.limit = limit;
	}

	public int getLimit() {
		return limit;
	}

	public String getValue() {
		return value;
	}

}
