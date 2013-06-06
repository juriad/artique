package cz.artique.shared.model.config;

import cz.artique.shared.utils.GenKey;
import cz.artique.shared.utils.SharedUtils;

public enum ConfigKey implements GenKey {
	MAX_ERROR_SEQUENCE("check.normal.max-error-sequence", ConfigType.INT, 5),
	DIFF_EDIT_COST("crawler.diff.edit-cost", ConfigType.INT, 10),
	DIFF_TIMEOUT("crawler.diff.timeout", ConfigType.DOUBLE, 0.1),
	MANUAL_SOURCE_NAME("source.manual.name", ConfigType.STRING, "manual"),
	EXPORT_FETCH_COUNT("export.fetch-count", ConfigType.INT, 30),
	CRAWLER_CHECK_INTERVAL_FIRST("crawler.check-interval.first",
			ConfigType.INT, 60 * 60 * 1000),
	CRAWLER_CHECK_INTERVAL_MIN("crawler.check-interval.min", ConfigType.INT,
			10 * 60 * 1000),
	CRAWLER_CHECK_INTERVAL_MAX("crawler.check-interval.max", ConfigType.INT,
			12 * 60 * 60 * 1000),
	CRAWLER_CHECK_INTERVAL_FAILED("crawler.check-interval.failed",
			ConfigType.INT, 5 * 60 * 1000);

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

	// same as Config.getKeyName
	public String getKeyName() {
		String prefix = "CONFIG";
		return SharedUtils.combineStringParts(prefix, getKey());
	}
}
