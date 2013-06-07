package cz.artique.shared.model.config;

import java.io.Serializable;

import cz.artique.shared.utils.GenKey;
import cz.artique.shared.utils.SharedUtils;

public enum ClientConfigKey implements GenKey, Serializable {
	SERVICE_TIMEOUT("service.timeout", new Value(2000)),
	LIST_INIT_SIZE("list.init_size", new Value(30)),
	LIST_FETCH_STEP("list.fetch_step", new Value(20)),
	LIST_FETCH_INTERVAL("list.fetch_interval", new Value(5000)),
	HISTORY_MAX_ITEMS("history.max_items", new Value(100)),
	MESSENGER_MAX_ITEMS("messanger.max_items", new Value(100)),
	SHOW_PANEL("ui.panel.show", new Value("F"));

	private final String key;
	private final Value defaultValue;
	private transient String userId;

	private ClientConfigKey(String key, Value defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
	}

	public String getKey() {
		return key;
	}

	public Value getDefaultValue() {
		return defaultValue;
	}

	public String getKeyName() {
		return SharedUtils.combineStringParts(getUserId(), getKey());
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public ConfigType getType() {
		return defaultValue.getType();
	}
}
