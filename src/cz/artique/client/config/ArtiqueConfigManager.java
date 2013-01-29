package cz.artique.client.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.manager.AbstractManager;
import cz.artique.client.service.ClientConfigService;
import cz.artique.client.service.ClientConfigServiceAsync;
import cz.artique.shared.model.config.ClientConfigKey;
import cz.artique.shared.model.config.ClientConfigValue;

public class ArtiqueConfigManager
		extends AbstractManager<ClientConfigServiceAsync>
		implements ConfigManager {
	
	public static final ArtiqueConfigManager MANAGER = new ArtiqueConfigManager();

	private Map<ClientConfigKey, ClientConfigValue<?>> configs =
		new HashMap<ClientConfigKey, ClientConfigValue<?>>();

	protected ArtiqueConfigManager() {
		super(GWT.<ClientConfigServiceAsync> create(ClientConfigService.class));
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
