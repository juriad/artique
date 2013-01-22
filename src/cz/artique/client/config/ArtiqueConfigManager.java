package cz.artique.client.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.AbstractManager;
import cz.artique.client.service.ClientConfigServiceAsync;
import cz.artique.shared.model.user.ClientConfigKey;
import cz.artique.shared.model.user.ClientConfigValue;

public class ArtiqueConfigManager
		extends AbstractManager<ClientConfigServiceAsync>
		implements ConfigManager {

	private Map<ClientConfigKey, ClientConfigValue<?>> configs =
		new HashMap<ClientConfigKey, ClientConfigValue<?>>();

	protected ArtiqueConfigManager(ClientConfigServiceAsync service) {
		super(service);
		refresh(null);
	}

	public void refresh(final AsyncCallback<Void> ping) {
		service
			.getClientConfigs(new AsyncCallback<List<ClientConfigValue<?>>>() {

				public void onFailure(Throwable caught) {
					if (ping != null) {
						ping.onFailure(null);
					}
				}

				public void onSuccess(List<ClientConfigValue<?>> result) {
					configs =
						new HashMap<ClientConfigKey, ClientConfigValue<?>>();
					for (ClientConfigValue<?> value : result) {
						configs.put(value.getKey(), value);
					}
					if (ping != null) {
						ping.onSuccess(null);
					}
					setReady();
				}
			});

	}

	public ClientConfigValue<?> getConfig(ClientConfigKey key) {
		return configs.get(key);
	}

	public void updateConfigValues(List<ClientConfigValue<?>> config,
			final AsyncCallback<Void> ping) {
		service.setClientConfigs(config,
			new AsyncCallback<List<ClientConfigValue<?>>>() {

				public void onFailure(Throwable caught) {
					if (ping != null) {
						ping.onFailure(null);
					}
				}

				public void onSuccess(List<ClientConfigValue<?>> result) {
					for (ClientConfigValue<?> value : result) {
						configs.put(value.getKey(), value);
					}
					if (ping != null) {
						ping.onSuccess(null);
					}
				}
			});
	}

}
