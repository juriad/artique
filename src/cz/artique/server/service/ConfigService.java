package cz.artique.server.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

import cz.artique.server.meta.config.ConfigMeta;
import cz.artique.server.utils.ServerUtils;
import cz.artique.shared.model.config.Config;
import cz.artique.shared.model.config.ConfigKey;
import cz.artique.shared.model.config.ConfigValue;

public enum ConfigService {
	CONFIG_SERVICE;

	private Map<ConfigKey, ConfigValue<?>> configs =
		new HashMap<ConfigKey, ConfigValue<?>>();

	private void refresh() {
		configs.clear();
		Map<String, ConfigKey> map = new HashMap<String, ConfigKey>();
		for (ConfigKey k : ConfigKey.values()) {
			map.put(k.getKey(), k);
		}

		ConfigMeta meta = ConfigMeta.get();
		List<Config> list = Datastore.query(meta).asList();

		for (Config config : list) {
			ConfigKey configKey = map.get(config.getConfigKey());
			if (configKey == null) {
				continue;
			}

			final ConfigValue<?> value = getValue(configKey, config);
			if (value == null) {
				continue;
			}
			configs.put(configKey, value);
		}

		// add those with default value
		for (ConfigKey configKey : ConfigKey.values()) {
			if (!configs.containsKey(configKey)) {
				final ConfigValue<?> value = getValue(configKey, null);
				configs.put(configKey, value);
			}
		}
	}

	private ConfigValue<?> getValue(ConfigKey configKey, Config config) {
		final ConfigValue<?> value;
		switch (configKey.getType()) {
		case DOUBLE:
			value =
				new ConfigValue<Double>(configKey, config == null
					? null
					: config.getDoubleValue());
			break;
		case INT:
			value =
				new ConfigValue<Integer>(configKey, config == null
					? null
					: config.getIntValue());
			break;
		case STRING:
			value =
				new ConfigValue<String>(configKey, config == null
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

	public ConfigValue<?> getConfig(ConfigKey key) {
		ConfigValue<?> configValue = configs.get(key);
		if (configValue == null) {
			refresh();
			configValue = configs.get(key);
		}
		return configValue;
	}

	public void setConfigs(List<ConfigValue<?>> changedConfigs) {
		Map<Key, ConfigValue<?>> keysToChange =
			new HashMap<Key, ConfigValue<?>>();
		List<Key> keysToDelete = new ArrayList<Key>();
		for (ConfigValue<?> config : changedConfigs) {
			Key key = ServerUtils.genKey(config.getKey());
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

		List<Config> changed = new ArrayList<Config>();

		ConfigMeta meta = ConfigMeta.get();
		Map<Key, Config> map = Datastore.getAsMap(meta, keysToChange.keySet());
		for (Key key : map.keySet()) {
			Config config = map.get(key);
			if (config == null) {
				// processed in the next round
				continue;
			}
			ConfigValue<?> value = keysToChange.remove(key);
			setValue(value, config);
			changed.add(config);
		}

		for (Key key : keysToChange.keySet()) {
			ConfigValue<?> value = keysToChange.get(key);
			Config config = new Config(value.getKey().getKey());
			config.setKey(key);
			setValue(value, config);
		}
		if (!changed.isEmpty()) {
			Datastore.put(changed);
		}
	}

	private void setValue(ConfigValue<?> value, Config config) {
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
