package cz.artique.shared.utils;

import java.io.Serializable;

/**
 * Property value is invalid
 */
public class PropertyValueException extends ArtiqueException
		implements Serializable {

	private static final long serialVersionUID = 1L;
	private final String property;
	private Object value;
	private Object reason;

	public PropertyValueException(String property, String value, String reason) {
		super();
		this.property = property;
		this.setValue(value);
		this.setReason(reason);
	}

	public String getProperty() {
		return property;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Object getReason() {
		return reason;
	}

	public void setReason(Object reason) {
		this.reason = reason;
	}
}
