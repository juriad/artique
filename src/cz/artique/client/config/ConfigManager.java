package cz.artique.client.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.manager.AbstractManager;
import cz.artique.client.manager.Manager;
import cz.artique.client.messages.MessageType;
import cz.artique.client.messages.ValidationMessage;
import cz.artique.client.service.ClientConfigService;
import cz.artique.client.service.ClientConfigService.GetClientConfigs;
import cz.artique.client.service.ClientConfigService.SetClientConfigs;
import cz.artique.client.service.ClientConfigServiceAsync;
import cz.artique.shared.model.config.client.ClientConfigKey;
import cz.artique.shared.model.config.client.ClientConfigValue;

/**
 * Manager which wraps {@link ClientConfigService} and provides related methods
 * to client part of application.
 * 
 * @author Adam Juraszek
 * 
 */
public class ConfigManager extends AbstractManager<ClientConfigServiceAsync>
		implements Manager {

	public static final ConfigManager MANAGER = new ConfigManager();

	private Map<ClientConfigKey, ClientConfigValue> configs =
		new HashMap<ClientConfigKey, ClientConfigValue>();

	protected ConfigManager() {
		super(GWT.<ClientConfigServiceAsync> create(ClientConfigService.class));
		refresh(null);
	}

	/**
	 * Refresh cached configuration by calling
	 * {@link ClientConfigService#getClientConfigs()}
	 * 
	 * @see cz.artique.client.manager.Manager#refresh(com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void refresh(final AsyncCallback<Void> ping) {
		assumeOnline();
		service.getClientConfigs(new AsyncCallback<List<ClientConfigValue>>() {
			public void onFailure(Throwable caught) {
				serviceFailed(caught);
				new ValidationMessage<GetClientConfigs>(
					GetClientConfigs.GENERAL).onFailure(caught);
				if (ping != null) {
					ping.onFailure(null);
				}
			}

			public void onSuccess(List<ClientConfigValue> result) {
				configs = new HashMap<ClientConfigKey, ClientConfigValue>();
				for (ClientConfigValue value : result) {
					configs.put(value.getKey(), value);
				}
				new ValidationMessage<GetClientConfigs>(
					GetClientConfigs.GENERAL).onSuccess(MessageType.DEBUG);
				if (ping != null) {
					ping.onSuccess(null);
				}
				setReady();
			}
		});

	}

	/**
	 * Returns configuration value from cache.
	 * 
	 * @param key
	 *            configuration key
	 * @return configuration value
	 */
	public ClientConfigValue getConfig(ClientConfigKey key) {
		return configs.get(key);
	}

	/**
	 * Updates configuration values by calling
	 * {@link ClientConfigService#setClientConfigs(List)}
	 * 
	 * @param config
	 *            list of changed values
	 * @param ping
	 *            notification about result
	 */
	public void updateConfigValues(List<ClientConfigValue> config,
			final AsyncCallback<Void> ping) {
		assumeOnline();
		service.setClientConfigs(config,
			new AsyncCallback<List<ClientConfigValue>>() {

				public void onFailure(Throwable caught) {
					serviceFailed(caught);
					new ValidationMessage<SetClientConfigs>(
						SetClientConfigs.GENERAL).onFailure(caught);
					if (ping != null) {
						ping.onFailure(null);
					}
				}

				public void onSuccess(List<ClientConfigValue> result) {
					for (ClientConfigValue value : result) {
						configs.put(value.getKey(), value);
					}
					new ValidationMessage<SetClientConfigs>(
						SetClientConfigs.GENERAL).onSuccess();
					if (ping != null) {
						ping.onSuccess(null);
					}
				}
			});
	}

}
