package cz.artique.server.service;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

import cz.artique.server.meta.user.ClientConfigMeta;
import cz.artique.server.utils.ServerUtils;
import cz.artique.shared.model.user.DefaultValue;
import cz.artique.shared.model.user.ClientConfig;
import cz.artique.shared.model.user.ClientConfigOption;

public class UserConfigService {

	private User user;

	public UserConfigService(User user) {
		this.user = user;
	}

	public long getLongValue(ClientConfigOption option) {
		return getUserConfig(option).getLongValue();
	}

	public void setLongValue(ClientConfigOption option, long value) {
		ClientConfig c = new ClientConfig(option.getConfigKey(), user);
		c.setLongValue(value);
		c.setKey(ServerUtils.genKey(c));
		Datastore.put(c);
	}

	public double getDoubleValue(ClientConfigOption option) {
		return getUserConfig(option).getDoubleValue();
	}

	public void setDoubleValue(ClientConfigOption option, double value) {
		ClientConfig c = new ClientConfig(option.getConfigKey(), user);
		c.setDoubleValue(value);
		c.setKey(ServerUtils.genKey(c));
		Datastore.put(c);
	}

	public String getStringValue(ClientConfigOption option) {
		return getUserConfig(option).getStringValue();
	}

	public void setStringValue(ClientConfigOption option, String value) {
		ClientConfig c = new ClientConfig(option.getConfigKey(), user);
		c.setStringValue(value);
		c.setKey(ServerUtils.genKey(c));
		Datastore.put(c);
	}

	private DefaultValue getUserConfig(ClientConfigOption option) {
		ClientConfigMeta meta = ClientConfigMeta.get();
		option.setUser(user);
		Key key = ServerUtils.genKey(option);
		ClientConfig config = Datastore.getOrNull(meta, key);
		if (config == null) {
			return option.getDefaultValue();
		}
		return config;
	}
}
