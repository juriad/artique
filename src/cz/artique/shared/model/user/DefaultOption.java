package cz.artique.shared.model.user;

public class DefaultOption {
	private Long intValue;
	private String stringValue;
	private Double floatValue;
	private final ConfigType type;

	public DefaultOption(Double f) {
		this.setFloatValue(f);
		type = ConfigType.FLOAT_CONFIG;
	}

	public DefaultOption(Long i) {
		this.setIntValue(i);
		type = ConfigType.INT_CONFIG;
	}

	public DefaultOption(String s) {
		this.setStringValue(s);
		type = ConfigType.STRING_CONFIG;
	}

	public Double getFloatValue() {
		return floatValue;
	}

	public Long getIntValue() {
		return intValue;
	}

	public String getStringValue() {
		return stringValue;
	}

	public ConfigType getType() {
		return type;
	}

	public void setFloatValue(Double floatValue) {
		this.floatValue = floatValue;
	}

	public void setIntValue(Long intValue) {
		this.intValue = intValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}
}
