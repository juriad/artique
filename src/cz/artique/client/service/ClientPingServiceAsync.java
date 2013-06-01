package cz.artique.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ClientPingServiceAsync {

	void ping(AsyncCallback<Void> callback);

}
