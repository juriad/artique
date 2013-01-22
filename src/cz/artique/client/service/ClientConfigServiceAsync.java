package cz.artique.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.shared.model.user.ClientConfigValue;

public interface ClientConfigServiceAsync {

	void getClientConfigs(AsyncCallback<List<ClientConfigValue<?>>> callback);

	void setClientConfigs(List<ClientConfigValue<?>> configs,
			AsyncCallback<List<ClientConfigValue<?>>> callback);

}
