package cz.artique.shared.model.user;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

import cz.artique.server.utils.ServerUtils;

public enum UserConfigOption implements DefaultValue {
	;

	private final String configKey;
	private final DefaultValue def;
	private final UserConfig userConfig;

	private UserConfigOption(String configKey, DefaultValue def) {
		this.configKey = configKey;
		this.def = def;
		UserConfig c = new UserConfig(configKey, null);
		this.userConfig = c;
	}

	public String getConfigKey() {
		return configKey;
	}

	public long getLongValue() {
		return def.getLongValue();
	}

	public double getDoubleValue() {
		return def.getDoubleValue();
	}

	public String getStringValue() {
		return def.getStringValue();
	}

	public DefaultValue getDefaultValue() {
		return def;
	}

	public Key getKey(User user) {
		userConfig.setUser(user);
		Key key = ServerUtils.genKey(userConfig);
		return key;
	}
}
