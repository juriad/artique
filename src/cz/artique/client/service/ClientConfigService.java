package cz.artique.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.artique.shared.model.config.client.ClientConfigValue;
import cz.artique.shared.validation.HasIssue;

@RemoteServiceRelativePath("service.s3gwt")
public interface ClientConfigService extends RemoteService {
	public enum GetClientConfigs implements HasIssue {
		GENERAL;
		public String enumName() {
			return "GetClientConfigs";
		}
	}

	List<ClientConfigValue> getClientConfigs();

	public enum SetClientConfigs implements HasIssue {
		GENERAL;
		public String enumName() {
			return "SetClientConfigs";
		}
	}

	List<ClientConfigValue> setClientConfigs(List<ClientConfigValue> configs);
}
