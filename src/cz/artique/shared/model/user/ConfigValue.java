package cz.artique.shared.model.user;

public class ConfigValue<E> {
	private E value;
	private ConfigKey key;
	private E originalValue;

	public ConfigValue(ConfigKey key, E value) {
		this.setKey(key);
		this.value = value;
		this.originalValue = value;
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

	public ConfigKey getKey() {
		return key;
	}

	public void setKey(ConfigKey key) {
		this.key = key;
	}
}
