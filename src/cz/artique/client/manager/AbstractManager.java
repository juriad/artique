package cz.artique.client.manager;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.http.client.RequestTimeoutException;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import cz.artique.client.ArtiqueConstants;
import cz.artique.client.ArtiqueWorld;
import cz.artique.client.i18n.I18n;
import cz.artique.client.messages.Message;
import cz.artique.client.messages.MessageType;

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

	private List<ManagerReady> waitingForReady = new ArrayList<ManagerReady>();

	protected synchronized void setReady() {
		if (isReady()) {
			return;
		}
		List<ManagerReady> pings = waitingForReady;
		waitingForReady = null;
		for (ManagerReady ping : pings) {
			ping.onReady();
		}
	}

	public synchronized void ready(ManagerReady ping) {
		if (waitingForReady == null) {
			ping.onReady();
		} else {
			waitingForReady.add(ping);
		}
	}

	public synchronized boolean isReady() {
		return waitingForReady == null;
	}

	protected void assumeOnline() {
		if (!ArtiqueWorld.WORLD.isOnline()) {
			ArtiqueConstants constant = I18n.getArtiqueConstants();
			Message message =
				new Message(MessageType.ERROR, constant.offline());
			Managers.MESSAGES_MANAGER.addMessage(message, false);
			throw new RuntimeException("Assumed online");
		}
	}

	protected void serviceFailed(Throwable caught) {
		if (caught instanceof RequestTimeoutException) {
			ArtiqueWorld.WORLD.testOnline();
		}
	}

}
