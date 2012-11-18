package cz.artique.shared.model.user;

public class StringValue implements DefaultValue {
	private final String value;

	public StringValue(String value) {
		this.value = value;
	}

	public long getLongValue() {
		return 0;
	}

	public double getDoubleValue() {
		return 0;
	}

	public String getStringValue() {
		return value;
	}
}
