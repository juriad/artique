package cz.artique.client.manager;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Manager is primarily class providing access to Client*Services.
 * Because some {@link Manager}s require getting list of existing data before
 * they can start serving, {@link Manager} is ready after all initialization
 * processes end.
 * 
 * @author Adam Juraszek
 * 
 */
public interface Manager {
	/**
	 * Forces refresh of managed data.
	 * 
	 * @param ping
	 *            callback when finished
	 */
	void refresh(AsyncCallback<Void> ping);

	/**
	 * @param timeout
	 *            timeout of rpc request
	 */
	void setTimeout(int timeout);

	/**
	 * @return timeout of rpc request
	 */
	int getTimeout();

	/**
	 * Manager will call the callback as soon as it become ready.
	 * 
	 * @param ping
	 *            callback
	 */
	void ready(ManagerReady ping);

	/**
	 * @return whether Manager is ready to serve
	 */
	boolean isReady();
}
