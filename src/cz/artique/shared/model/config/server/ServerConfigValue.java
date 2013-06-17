package cz.artique.shared.model.config.server;

/**
 * Represents value of server config.
 * 
 * @author Adam Juraszek
 * 
 * @param <E>
 *            either Integer, Double or String
 */
public class ServerConfigValue<E> {
	private E value;
	private ServerConfigKey key;

	/**
	 * Constructor setting config key and value.
	 * 
	 * @param key
	 * @param value
	 */
	public ServerConfigValue(ServerConfigKey key, E value) {
		this.setKey(key);
		this.value = value;
	}

	/**
	 * @return value
	 */
	public E getValue() {
		return value;
	}

	/**
	 * @return value if exists, default value otherwise
	 */
	@SuppressWarnings("unchecked")
	public <T> T get() {
		if (value != null) {
			return (T) value;
		} else {
			return getDefaultValue();
		}
	}

	/**
	 * @return default value
	 */
	public <T> T getDefaultValue() {
		return getKey().getDefaultValue();
	}

	/**
	 * @param value
	 *            value
	 */
	public void setValue(E value) {
		this.value = value;
	}

	/**
	 * @return key
	 */
	public ServerConfigKey getKey() {
		return key;
	}

	/**
	 * @param key
	 *            key
	 */
	public void setKey(ServerConfigKey key) {
		this.key = key;
	}
}
