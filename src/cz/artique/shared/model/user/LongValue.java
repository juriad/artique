package cz.artique.shared.model.user;

public class LongValue implements DefaultValue {
	private final long value;

	public LongValue(long value) {
		this.value = value;
	}

	public long getLongValue() {
		return value;
	}

	public double getDoubleValue() {
		return 0;
	}

	public String getStringValue() {
		return "";
	}
}
