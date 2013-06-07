package cz.artique.server.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

import cz.artique.client.service.ClientConfigService;
import cz.artique.server.meta.config.ClientConfigMeta;
import cz.artique.server.utils.KeyGen;
import cz.artique.shared.model.config.ClientConfig;
import cz.artique.shared.model.config.ClientConfigKey;
import cz.artique.shared.model.config.ClientConfigValue;
import cz.artique.shared.model.config.Value;

public class ClientConfigServiceImpl implements ClientConfigService {

	public List<ClientConfigValue> getClientConfigs() {
		String userId = UserService.getCurrentUserId();

		Map<ClientConfigKey, ClientConfigValue> configs =
			new HashMap<ClientConfigKey, ClientConfigValue>();

		Map<String, ClientConfigKey> map =
			new HashMap<String, ClientConfigKey>();
		for (ClientConfigKey k : ClientConfigKey.values()) {
			map.put(k.getKey(), k);
		}

		ClientConfigMeta meta = ClientConfigMeta.get();
		List<ClientConfig> list =
			Datastore.query(meta).filter(meta.userId.equal(userId)).asList();

		for (ClientConfig config : list) {
			ClientConfigKey configKey = map.get(config.getConfigKey());
			if (configKey == null) {
				continue;
			}

			final ClientConfigValue value = getValue(configKey, config);
			if (value == null) {
				continue;
			}
			configs.put(configKey, value);
		}

		// add those with default value
		for (ClientConfigKey configKey : ClientConfigKey.values()) {
			if (!configs.containsKey(configKey)) {
				final ClientConfigValue value = getValue(configKey, null);
				configs.put(configKey, value);
			}
		}

		return new ArrayList<ClientConfigValue>(configs.values());
	}

	private ClientConfigValue getValue(ClientConfigKey configKey,
			ClientConfig config) {
		final ClientConfigValue value;
		switch (configKey.getType()) {
		case DOUBLE:
			value =
				new ClientConfigValue(configKey, config == null
					? null
					: new Value(config.getDoubleValue()));
			break;
		case INT:
			value =
				new ClientConfigValue(configKey, config == null
					? null
					: new Value(config.getIntValue()));
			break;
		case STRING:
			value =
				new ClientConfigValue(configKey, config == null
					? null
					: new Value(config.getStringValue()));
			break;
		default:
			// no other type is supported
			value = null;
			break;
		}
		return value;
	}

	public List<ClientConfigValue> setClientConfigs(
			List<ClientConfigValue> configs) {
		String userId = UserService.getCurrentUserId();

		Map<Key, ClientConfigValue> keysToChange =
			new HashMap<Key, ClientConfigValue>();
		List<Key> keysToDelete = new ArrayList<Key>();
		for (ClientConfigValue config : configs) {
			ClientConfigKey configKey = config.getKey();
			configKey.setUserId(userId);
			Key key = KeyGen.genKey(configKey);
			configKey.setUserId(null);
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

		List<ClientConfig> changed = new ArrayList<ClientConfig>();

		ClientConfigMeta meta = ClientConfigMeta.get();
		Map<Key, ClientConfig> map =
			Datastore.getAsMap(meta, keysToChange.keySet());
		for (Key key : map.keySet()) {
			ClientConfig config = map.get(key);
			if (config == null) {
				// processed in the next round
				continue;
			}
			ClientConfigValue value = keysToChange.remove(key);
			setValue(value, config);
			changed.add(config);
		}

		for (Key key : keysToChange.keySet()) {
			ClientConfigValue value = keysToChange.get(key);
			ClientConfig config =
				new ClientConfig(userId, value.getKey().getKey());
			config.setKey(key);
			setValue(value, config);
		}
		if (!changed.isEmpty()) {
			Datastore.put(changed);
		}

		return getClientConfigs();
	}

	private void setValue(ClientConfigValue value, ClientConfig config) {
		switch (value.getKey().getType()) {
		case DOUBLE:
			config.setDoubleValue(value.get().getD());
			break;
		case INT:
			config.setIntValue(value.get().getI());
			break;
		case STRING:
			config.setStringValue(value.get().getS());
			break;
		default:
			// no other type is supported
			break;
		}
	}

}
