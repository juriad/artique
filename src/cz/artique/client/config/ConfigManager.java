package cz.artique.client.config;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.Manager;
import cz.artique.shared.model.user.ClientConfigKey;
import cz.artique.shared.model.user.ClientConfigValue;

public interface ConfigManager extends Manager {
	ClientConfigValue<?> getConfig(ClientConfigKey key);

	void updateConfigValues(List<ClientConfigValue<?>> config,
			AsyncCallback<Void> ping);
}
