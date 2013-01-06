package cz.artique.shared.model.user;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

import cz.artique.shared.utils.GenKey;
import cz.artique.shared.utils.SharedUtils;

public enum UserConfigOption implements DefaultValue, GenKey {
	;

	private final String configKey;
	private final DefaultValue def;

	private transient User user;
	private transient Key key;

	private UserConfigOption(String configKey, DefaultValue def) {
		this.configKey = configKey;
		this.def = def;
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

	public Key getKeyParent() {
		return null;
	}

	public String getKeyName() {
		String prefix = "USER-CONFIG";
		String userId = user.getUserId();
		return SharedUtils.combineStringParts(prefix, userId, getConfigKey());
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}
}
