package cz.artique.shared.utils;


public class PropertyEmptyException extends PropertyValueException {

	private static final long serialVersionUID = 1L;

	public PropertyEmptyException(String property) {
		super(property, "", "property is empty");
	}
}
