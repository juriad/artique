package cz.artique.shared.model.config.server;

import cz.artique.shared.model.config.ConfigType;
import cz.artique.shared.utils.GenKey;
import cz.artique.shared.utils.SharedUtils;

/**
 * Configuration keys for server config. Simulates {@link ServerConfig#getKey()}
 * 
 * @author Adam Juraszek
 * 
 */
public enum ServerConfigKey implements GenKey {
	/**
	 * Maximal number of tries before giving up checking the source normal way.
	 */
	MAX_ERROR_SEQUENCE("check.normal.max-error-sequence", ConfigType.INT, 5),
	/**
	 * Describes how short pieces in diff may be.
	 */
	DIFF_EDIT_COST("crawler.diff.edit-cost", ConfigType.INT, 10),
	/**
	 * Calculate timeout maximally this long.
	 */
	DIFF_TIMEOUT("crawler.diff.timeout", ConfigType.DOUBLE, 0.1),
	/**
	 * Default name for manual source.
	 */
	MANUAL_SOURCE_NAME("source.manual.name", ConfigType.STRING, "manual"),
	/**
	 * Export at most this number of items.
	 */
	EXPORT_FETCH_COUNT("export.fetch-count", ConfigType.INT, 30),
	/**
	 * Interval of first source check.
	 */
	CRAWLER_CHECK_INTERVAL_FIRST("crawler.check-interval.first",
			ConfigType.INT, 60 * 60 * 1000),
	/**
	 * Minimal source check interval.
	 */
	CRAWLER_CHECK_INTERVAL_MIN("crawler.check-interval.min", ConfigType.INT,
			10 * 60 * 1000),
	/**
	 * Maximal source check interval.
	 */
	CRAWLER_CHECK_INTERVAL_MAX("crawler.check-interval.max", ConfigType.INT,
			12 * 60 * 60 * 1000),
	/**
	 * Interval before trying check again if current check failed.
	 */
	CRAWLER_CHECK_INTERVAL_FAILED("crawler.check-interval.failed",
			ConfigType.INT, 5 * 60 * 1000),
	/**
	 * Number of iteration in calculation of recommendations.
	 */
	RECOMMENDATION_ITERATIONS("recommendation.iterations", ConfigType.INT, 5),
	/**
	 * Number of recommendations to show to user.
	 */
	RECOMMENDATION_COUNT("recommendation.count", ConfigType.INT, 10);

	private final ConfigType type;
	private final String key;
	private final Object defaultValue;

	private ServerConfigKey(String key, ConfigType type, Object defaultValue) {
		this.key = key;
		this.type = type;
		this.defaultValue = defaultValue;
	}

	/**
	 * @return type of config
	 */
	public ConfigType getType() {
		return type;
	}

	/**
	 * @return key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return typeless default value
	 */
	@SuppressWarnings("unchecked")
	public <T> T getDefaultValue() {
		return (T) defaultValue;
	}

	/**
	 * same as Config.getKeyName
	 * 
	 * @return unique key name
	 */
	public String getKeyName() {
		String prefix = "CONFIG";
		return SharedUtils.combineStringParts(prefix, getKey());
	}
}
