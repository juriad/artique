package cz.artique.shared.model.config.client;

import java.io.Serializable;

import cz.artique.shared.model.config.ConfigType;
import cz.artique.shared.model.config.ConfigValue;
import cz.artique.shared.utils.GenKey;
import cz.artique.shared.utils.SharedUtils;

/**
 * Configuration keys for client config. Simulates {@link ClientConfig#getKey()}
 * 
 * @author Adam Juraszek
 * 
 */
public enum ClientConfigKey implements GenKey, Serializable {
	/**
	 * Timeout of all services. If service timeouts, message is shown and all
	 * services are suspended.
	 */
	SERVICE_TIMEOUT("service.timeout", new ConfigValue(3000)),
	/**
	 * Number of items to show when the listing is loaded.
	 */
	LIST_INIT_SIZE("list.init_size", new ConfigValue(60)),
	/**
	 * Number of items to fetch each time user scrolls to the end.
	 */
	LIST_FETCH_STEP("list.fetch_step", new ConfigValue(60)),
	/**
	 * How often is performed check for new items.
	 */
	LIST_FETCH_INTERVAL("list.fetch_interval", new ConfigValue(10000)),
	/**
	 * Maximal number of items in history to show.
	 */
	HISTORY_MAX_ITEMS("history.max_items", new ConfigValue(100)),
	/**
	 * Maximal number of items in messages to show.
	 */
	MESSENGER_MAX_ITEMS("messanger.max_items", new ConfigValue(100)),
	/**
	 * Which panel to show when the application is initialized.
	 */
	SHOW_PANEL("ui.panel.show", new ConfigValue("F"));

	private final String key;
	private final ConfigValue defaultValue;
	private transient String userId;

	private ClientConfigKey(String key, ConfigValue defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
	}

	/**
	 * @return key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return default value
	 */
	public ConfigValue getDefaultValue() {
		return defaultValue;
	}

	public String getKeyName() {
		return SharedUtils.combineStringParts(getUserId(), getKey());
	}

	/**
	 * @return owner of this option; user is set only temporarily
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            owner of this option; user is set only temporarily
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return type of config
	 */
	public ConfigType getType() {
		return defaultValue.getType();
	}
}
