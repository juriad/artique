package cz.artique.server.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

import cz.artique.server.meta.config.server.ServerConfigMeta;
import cz.artique.server.utils.KeyGen;
import cz.artique.shared.model.config.server.ServerConfig;
import cz.artique.shared.model.config.server.ServerConfigKey;
import cz.artique.shared.model.config.server.ServerConfigValue;

public enum ConfigService {
	CONFIG_SERVICE;

	private Map<ServerConfigKey, ServerConfigValue<?>> configs =
		new HashMap<ServerConfigKey, ServerConfigValue<?>>();

	private void refresh() {
		configs.clear();
		Map<String, ServerConfigKey> map =
			new HashMap<String, ServerConfigKey>();
		for (ServerConfigKey k : ServerConfigKey.values()) {
			map.put(k.getKey(), k);
		}

		ServerConfigMeta meta = ServerConfigMeta.get();
		List<ServerConfig> list = Datastore.query(meta).asList();

		for (ServerConfig config : list) {
			ServerConfigKey configKey = map.get(config.getConfigKey());
			if (configKey == null) {
				continue;
			}

			final ServerConfigValue<?> value = getValue(configKey, config);
			if (value == null) {
				continue;
			}
			configs.put(configKey, value);
		}

		// add those with default value
		for (ServerConfigKey configKey : ServerConfigKey.values()) {
			if (!configs.containsKey(configKey)) {
				final ServerConfigValue<?> value = getValue(configKey, null);
				configs.put(configKey, value);
			}
		}
	}

	private ServerConfigValue<?> getValue(ServerConfigKey configKey,
			ServerConfig config) {
		final ServerConfigValue<?> value;
		switch (configKey.getType()) {
		case DOUBLE:
			value =
				new ServerConfigValue<Double>(configKey, config == null
					? null
					: config.getDoubleValue());
			break;
		case INT:
			value =
				new ServerConfigValue<Integer>(configKey, config == null
					? null
					: config.getIntValue());
			break;
		case STRING:
			value =
				new ServerConfigValue<String>(configKey, config == null
					? null
					: config.getStringValue());
			break;
		default:
			// no other type is supported
			value = null;
			break;
		}
		return value;
	}

	public ServerConfigValue<?> getConfig(ServerConfigKey key) {
		ServerConfigValue<?> configValue = configs.get(key);
		if (configValue == null) {
			refresh();
			configValue = configs.get(key);
		}
		return configValue;
	}

	public void setConfigs(List<ServerConfigValue<?>> changedConfigs) {
		Map<Key, ServerConfigValue<?>> keysToChange =
			new HashMap<Key, ServerConfigValue<?>>();
		List<Key> keysToDelete = new ArrayList<Key>();
		for (ServerConfigValue<?> config : changedConfigs) {
			Key key = KeyGen.genKey(config.getKey());
			if (config.getDefaultValue().equals(config.getValue())
				|| config.getValue() == null) {
				keysToDelete.add(key);
			} else {
				keysToChange.put(key, config);
			}
		}

		if (!keysToDelete.isEmpty()) {
			Datastore.delete(keysToDelete);
		}

		List<ServerConfig> changed = new ArrayList<ServerConfig>();

		ServerConfigMeta meta = ServerConfigMeta.get();
		Map<Key, ServerConfig> map =
			Datastore.getAsMap(meta, keysToChange.keySet());
		for (Key key : map.keySet()) {
			ServerConfig config = map.get(key);
			if (config == null) {
				// processed in the next round
				continue;
			}
			ServerConfigValue<?> value = keysToChange.remove(key);
			setValue(value, config);
			changed.add(config);
		}

		for (Key key : keysToChange.keySet()) {
			ServerConfigValue<?> value = keysToChange.get(key);
			ServerConfig config = new ServerConfig(value.getKey().getKey());
			config.setKey(key);
			setValue(value, config);
		}
		if (!changed.isEmpty()) {
			Datastore.put(changed);
		}
	}

	private void setValue(ServerConfigValue<?> value, ServerConfig config) {
		switch (value.getKey().getType()) {
		case DOUBLE:
			config.setDoubleValue(value.<Double> get());
			break;
		case INT:
			config.setIntValue(value.<Integer> get());
			break;
		case STRING:
			config.setStringValue(value.<String> get());
			break;
		default:
			// no other type is supported
			break;
		}
	}
}
