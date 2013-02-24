package cz.artique.shared.model.config;

import com.google.appengine.api.datastore.Key;

import cz.artique.shared.utils.GenKey;
import cz.artique.shared.utils.SharedUtils;

public enum ConfigKey implements GenKey {
	MAX_ERROR_SEQUENCE("check.normal.max-error-sequence", ConfigType.INT, 5),
	DIFF_EDIT_COST("crawler.diff.edit-cost", ConfigType.INT, 10),
	DIFF_TIMEOUT("crawler.diff.timeout", ConfigType.DOUBLE, 0.1),
	MANUAL_SOURCE_NAME("source.manual.name", ConfigType.STRING, "manual");

	private final ConfigType type;
	private final String key;
	private final Object defaultValue;

	private ConfigKey(String key, ConfigType type, Object defaultValue) {
		this.key = key;
		this.type = type;
		this.defaultValue = defaultValue;
	}

	public ConfigType getType() {
		return type;
	}

	public String getKey() {
		return key;
	}

	@SuppressWarnings("unchecked")
	public <T> T getDefaultValue() {
		return (T) defaultValue;
	}

	public Key getKeyParent() {
		return null;
	}

	// same as Config.getKeyName
	public String getKeyName() {
		String prefix = "CONFIG";
		return SharedUtils.combineStringParts(prefix, getKey());
	}
}
