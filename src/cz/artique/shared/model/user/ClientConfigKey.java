package cz.artique.shared.model.user;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

import cz.artique.shared.utils.GenKey;
import cz.artique.shared.utils.SharedUtils;

public enum ClientConfigKey implements GenKey {
	KEY1("key1.key1.1", ConfigType.STRING, "hopsa hejsa");

	private final ConfigType type;
	private final String key;
	private final Object defaultValue;
	private transient User user;

	private ClientConfigKey(String key, ConfigType type, Object defaultValue) {
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
	
	public String getKeyName() {
		return SharedUtils.combineStringParts(getUser().getUserId(),
			getKey());
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
