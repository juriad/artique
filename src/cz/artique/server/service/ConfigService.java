package cz.artique.server.service;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

import cz.artique.server.meta.user.ConfigMeta;
import cz.artique.server.utils.ServerUtils;
import cz.artique.shared.model.user.Config;
import cz.artique.shared.model.user.ConfigOption;
import cz.artique.shared.model.user.DefaultValue;

public class ConfigService {

	public ConfigService() {}

	public long getLongValue(ConfigOption option) {
		return getConfig(option).getLongValue();
	}

	public void setLongValue(ConfigOption option, long value) {
		Config c = new Config(option.getConfigKey());
		c.setLongValue(value);
		c.setKey(ServerUtils.genKey(c));
		Datastore.put(c);
	}

	public double getDoubleValue(ConfigOption option) {
		return getConfig(option).getDoubleValue();
	}

	public void setDoubleValue(ConfigOption option, double value) {
		Config c = new Config(option.getConfigKey());
		c.setDoubleValue(value);
		c.setKey(ServerUtils.genKey(c));
		Datastore.put(c);
	}

	public String getStringValue(ConfigOption option) {
		return getConfig(option).getStringValue();
	}

	public void setStringValue(ConfigOption option, String value) {
		Config c = new Config(option.getConfigKey());
		c.setStringValue(value);
		c.setKey(ServerUtils.genKey(c));
		Datastore.put(c);
	}

	private DefaultValue getConfig(ConfigOption option) {
		ConfigMeta meta = ConfigMeta.get();
		Key key = option.getKey();
		if (key == null) {
			key = ServerUtils.genKey(option);
			option.setKey(key);
		}
		Config config = Datastore.getOrNull(meta, key);
		if (config == null) {
			return option.getDefaultValue();
		}
		return config;
	}
}
