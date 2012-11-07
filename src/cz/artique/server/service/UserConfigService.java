package cz.artique.server.service;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

import cz.artique.shared.model.user.ConfigOption;
import cz.artique.shared.model.user.FloatConfig;
import cz.artique.shared.model.user.IntConfig;
import cz.artique.shared.model.user.StringConfig;

public class UserConfigService {

	private User user;

	public UserConfigService(User user) {
		this.user = user;
	}

	public Double getFloatConfig(ConfigOption option) {
		FloatConfig fc =
			(FloatConfig) Datastore.getOrNull(option.getType().getMeta(),
				getKey(option));
		return fc == null ? option.getDef().getFloatValue() : fc
			.getConfigValue();
	}

	public void getFloatConfig(ConfigOption option, Double value) {
		FloatConfig sc = new FloatConfig();
		sc.setKey(getKey(option));
		sc.setConfigKey(option.getConfigKey());
		sc.setUser(user);
		sc.setConfigValue(value);
		Datastore.put(sc);
	}

	public Long getIntConfig(ConfigOption option) {
		IntConfig ic =
			(IntConfig) Datastore.getOrNull(option.getType().getMeta(),
				getKey(option));
		return ic == null ? option.getDef().getIntValue() : ic.getConfigValue();
	}

	public void getIntConfig(ConfigOption option, Long value) {
		IntConfig ic = new IntConfig();
		ic.setKey(getKey(option));
		ic.setConfigKey(option.getConfigKey());
		ic.setUser(user);
		ic.setConfigValue(value);
		Datastore.put(ic);
	}

	private Key getKey(ConfigOption option) {
		return Datastore.createKey(option.getType().getMeta(),
			option.getConfigKey(user));
	}

	public String getStringConfig(ConfigOption option) {
		StringConfig sc =
			(StringConfig) Datastore.getOrNull(option.getType().getMeta(),
				getKey(option));
		return sc == null ? option.getDef().getStringValue() : sc
			.getConfigValue();
	}

	public void setStringConfig(ConfigOption option, String value) {
		StringConfig sc = new StringConfig();
		sc.setKey(getKey(option));
		sc.setConfigKey(option.getConfigKey());
		sc.setUser(user);
		sc.setConfigValue(value);
		Datastore.put(sc);
	}
}
