package cz.artique.server.service;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.users.User;

import cz.artique.server.meta.user.UserConfigMeta;
import cz.artique.server.utils.ServerUtils;
import cz.artique.shared.model.user.DefaultValue;
import cz.artique.shared.model.user.UserConfig;
import cz.artique.shared.model.user.UserConfigOption;

public class UserConfigService {

	private User user;

	public UserConfigService(User user) {
		this.user = user;
	}

	public long getLongValue(UserConfigOption option) {
		return getUserConfig(option).getLongValue();
	}

	public void setLongValue(UserConfigOption option, long value) {
		UserConfig c = new UserConfig(option.getConfigKey(), user);
		c.setLongValue(value);
		c.setKey(ServerUtils.genKey(c));
		Datastore.put(c);
	}

	public double getDoubleValue(UserConfigOption option) {
		return getUserConfig(option).getDoubleValue();
	}

	public void setDoubleValue(UserConfigOption option, double value) {
		UserConfig c = new UserConfig(option.getConfigKey(), user);
		c.setDoubleValue(value);
		c.setKey(ServerUtils.genKey(c));
		Datastore.put(c);
	}

	public String getStringValue(UserConfigOption option) {
		return getUserConfig(option).getStringValue();
	}

	public void setStringValue(UserConfigOption option, String value) {
		UserConfig c = new UserConfig(option.getConfigKey(), user);
		c.setStringValue(value);
		c.setKey(ServerUtils.genKey(c));
		Datastore.put(c);
	}

	private DefaultValue getUserConfig(UserConfigOption option) {
		UserConfigMeta meta = UserConfigMeta.get();
		UserConfig config = Datastore.getOrNull(meta, option.getKey(user));
		if (config == null) {
			return option.getDefaultValue();
		}
		return config;
	}
}
