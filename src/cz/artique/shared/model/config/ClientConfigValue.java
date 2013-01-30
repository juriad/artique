package cz.artique.shared.model.config;

import java.io.Serializable;

public class ClientConfigValue implements Serializable {
	private static final long serialVersionUID = 1L;
	private Value value;
	private ClientConfigKey key;
	private Value originalValue;

	public ClientConfigValue() {}

	public ClientConfigValue(ClientConfigKey key, Value value) {
		this.setKey(key);
		this.setValue(value);
		this.setOriginalValue(value);
	}

	public Value getDefaultValue() {
		return getKey().getDefaultValue();
	}

	public ClientConfigKey getKey() {
		return key;
	}

	public void setKey(ClientConfigKey key) {
		this.key = key;
	}

	public Value get() {
		return value == null ? getDefaultValue() : getValue();
	}

	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}

	public Value getOriginalValue() {
		return originalValue;
	}

	public void setOriginalValue(Value originalValue) {
		this.originalValue = originalValue;
	}
}
