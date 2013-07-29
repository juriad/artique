package cz.artique.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.artique.shared.model.config.client.ClientConfigValue;
import cz.artique.shared.validation.HasIssue;

/**
 * The service responsible for configuration passing and related operations
 * between client and server.
 * 
 * @author Adam Juraszek
 */
@RemoteServiceRelativePath("service.s3gwt")
public interface ClientConfigService extends RemoteService {
	public enum GetClientConfigs implements HasIssue {
		GENERAL;
		public String enumName() {
			return "GetClientConfigs";
		}
	}

	/**
	 * Gets list of all existing configuration options with their values for
	 * current user.
	 * If an option does not have value, default value is assumed.
	 * 
	 * @return list of all configuration values
	 */
	List<ClientConfigValue> getClientConfigs();

	public enum SetClientConfigs implements HasIssue {
		GENERAL;
		public String enumName() {
			return "SetClientConfigs";
		}
	}

	/**
	 * Sets new values for configuration options. The values will be updated in
	 * the database.
	 * 
	 * @param configs
	 *            list of configuration options with new values
	 * @return updated valuef of changed configuration options
	 */
	List<ClientConfigValue> setClientConfigs(List<ClientConfigValue> configs);
}
