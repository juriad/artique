package cz.artique.shared.model.config.client;

import java.io.Serializable;

import cz.artique.shared.model.config.ConfigValue;

/**
 * Represents value of client config.
 * 
 * @author Adam Juraszek
 * 
 */
public class ClientConfigValue implements Serializable {
	private static final long serialVersionUID = 1L;
	private ConfigValue value;
	private ClientConfigKey key;

	/**
	 * Default constructor which allows serialization by GWT.
	 */
	public ClientConfigValue() {}

	/**
	 * Constructor setting config key and config value.
	 * 
	 * @param key
	 * @param value
	 */
	public ClientConfigValue(ClientConfigKey key, ConfigValue value) {
		this.setKey(key);
		this.setValue(value);
	}

	/**
	 * @return default value
	 */
	public ConfigValue getDefaultValue() {
		return getKey().getDefaultValue();
	}

	/**
	 * @return key
	 */
	public ClientConfigKey getKey() {
		return key;
	}

	/**
	 * @param key
	 *            key
	 */
	public void setKey(ClientConfigKey key) {
		this.key = key;
	}

	/**
	 * @return value if exists, default value otherwise
	 */
	public ConfigValue get() {
		return value == null ? getDefaultValue() : getValue();
	}

	/**
	 * @return value
	 */
	public ConfigValue getValue() {
		return value;
	}

	/**
	 * @param value
	 *            value
	 */
	public void setValue(ConfigValue value) {
		this.value = value;
	}
}
