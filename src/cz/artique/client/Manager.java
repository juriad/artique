package cz.artique.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface Manager {
	public void refresh(AsyncCallback<Void> ping);

	public void setTimeout(int timeout);

	public int getTimeout();
}
