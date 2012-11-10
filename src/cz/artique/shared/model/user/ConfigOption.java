package cz.artique.shared.model.user;

import com.google.appengine.api.users.User;

public enum ConfigOption {
	LABEL_ASSIGNED_COLOR("server.theme.label.assigned-color",
			ConfigType.STRING_CONFIG, new DefaultOption("#ffffff")),
	LABEL_UN_ASSIGNED_COLOR("server.theme.label.unassigned-color",
			ConfigType.STRING_CONFIG, new DefaultOption("#ffffff")),
	LABEL_ASSIGNED_BACKGROUND("server.theme.label.assigned-background",
			ConfigType.STRING_CONFIG, new DefaultOption("#ffffff")),
	LABEL_UN_ASSIGNED_BACKGROUND("server.theme.label.unassigned-background",
			ConfigType.STRING_CONFIG, new DefaultOption("#ffffff"));

	private final String configKey;
	private final ConfigType type;
	private final DefaultOption def;

	private ConfigOption(String configKey, ConfigType type, DefaultOption def) {
		this.configKey = configKey;
		this.type = type;
		this.def = def;
	}

	public String getConfigKey() {
		return configKey;
	}

	public String getConfigKey(User user) {
		return configKey + "#" + user.getUserId();
	}

	public DefaultOption getDef() {
		return def;
	}

	public ConfigType getType() {
		return type;
	}

	// TODO find by configKey
	// TODO filter by prefix

}
