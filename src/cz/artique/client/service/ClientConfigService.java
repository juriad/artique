package cz.artique.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.artique.shared.model.user.ClientConfigValue;

@RemoteServiceRelativePath("service.s3gwt")
public interface ClientConfigService extends RemoteService {
	List<ClientConfigValue<?>> getClientConfigs();

	List<ClientConfigValue<?>> setClientConfigs(
			List<ClientConfigValue<?>> configs);
}
