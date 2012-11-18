package cz.artique.shared.model.user;

import com.google.appengine.api.datastore.Key;

import cz.artique.server.utils.ServerUtils;

public enum ConfigOption implements DefaultValue {
	;

	private final String configKey;
	private final DefaultValue def;
	private final Key key;

	private ConfigOption(String configKey, DefaultValue def) {
		this.configKey = configKey;
		this.def = def;
		Config c = new Config(configKey);
		this.key = ServerUtils.genKey(c);
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

	public Key getKey() {
		return key;
	}
}
