package cz.artique.shared.model.config;

public class ClientConfigValue<E> {
	private E value;
	private ClientConfigKey key;
	private E originalValue;

	public ClientConfigValue(ClientConfigKey key, E value) {
		this.setKey(key);
		this.value = value;
	}

	public E getValue() {
		return value;
	}

	@SuppressWarnings("unchecked")
	public <T> T get() {
		if (value != null) {
			return (T) value;
		} else {
			return getDefaultValue();
		}
	}

	public <T> T getDefaultValue() {
		return getKey().getDefaultValue();
	}

	public void setValue(E value) {
		this.value = value;
	}

	public E getOriginalValue() {
		return originalValue;
	}

	public void setOriginalValue(E originalValue) {
		this.originalValue = originalValue;
	}

	public ClientConfigKey getKey() {
		return key;
	}

	public void setKey(ClientConfigKey key) {
		this.key = key;
	}
}
