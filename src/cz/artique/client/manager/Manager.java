package cz.artique.client.manager;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface Manager {
	void refresh(AsyncCallback<Void> ping);

	void setTimeout(int timeout);

	int getTimeout();

	void ready(AsyncCallback<Void> ping);

	boolean isReady();
}
