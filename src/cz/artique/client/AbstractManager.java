package cz.artique.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public abstract class AbstractManager<E> implements Manager {

	protected E service;

	private int timeout;

	protected AbstractManager(E service) {
		this.service = service;
		timeout = 1000;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
		((ServiceDefTarget) service)
			.setRpcRequestBuilder(new RpcWithTimeoutRequestBuilder(getTimeout()));
	}

	public int getTimeout() {
		return timeout;
	}

	private List<AsyncCallback<Void>> waitingForReady =
		new ArrayList<AsyncCallback<Void>>();

	protected synchronized void setReady() {
		if (waitingForReady == null) {
			return;
		}
		List<AsyncCallback<Void>> pings = waitingForReady;
		waitingForReady = null;
		for (AsyncCallback<Void> ping : pings) {
			ping.onSuccess(null);
		}
	}

	public synchronized void ready(AsyncCallback<Void> ping) {
		if (waitingForReady == null) {
			ping.onSuccess(null);
		} else {
			waitingForReady.add(ping);
		}
	}

	public synchronized boolean isReady() {
		return waitingForReady == null;
	}

}
