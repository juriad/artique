package cz.artique.shared.model.user;

public class DoubleValue implements DefaultValue {
	private final double value;

	public DoubleValue(double value) {
		this.value = value;
	}

	public long getLongValue() {
		return 0;
	}

	public double getDoubleValue() {
		return value;
	}

	public String getStringValue() {
		return "";
	}
}
