package cz.artique.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.appengine.api.users.User;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.artiqueListing.ArtiqueList;
import cz.artique.client.config.ArtiqueConfigManager;
import cz.artique.client.manager.AbstractManager;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.config.ClientConfigKey;

public enum ArtiqueWorld {
	WORLD;

	private ArtiqueWorld() {
		for (final Managers m : Managers.values()) {
			m.get().ready(new AsyncCallback<Void>() {
				public void onSuccess(Void result) {
					ready.add(m);
					Iterator<WaitRequest> iter = waiting.iterator();
					while (iter.hasNext()) {
						WaitRequest r = iter.next();
						r.managers.remove(m);
						if (r.managers.isEmpty()) {
							iter.remove();
							r.ping.onSuccess(null);
						}
					}
				}

				public void onFailure(Throwable caught) {
					// TODO failed manager
				}
			});
		}
		waitForManager(new AsyncCallback<Void>() {

			public void onFailure(Throwable caught) {
				// ignore
			}

			public void onSuccess(Void result) {
				for (Managers m : Managers.values()) {
					if (m.get() instanceof AbstractManager) {
						((AbstractManager<?>) m.get()).setTimeout(ArtiqueConfigManager.MANAGER
							.getConfig(ClientConfigKey.SERVICE_TIMEOUT)
							.get()
							.getI());
					}
				}
			}
		}, Managers.CONFIG_MANAGER);
	}

	private UserInfo userInfo;
	private ArtiqueList list;

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public User getUser() {
		return getUserInfo().getUser();
	}

	public ArtiqueList getList() {
		return list;
	}

	public void setList(ArtiqueList list) {
		this.list = list;
	}

	private class WaitRequest {
		List<Managers> managers;
		AsyncCallback<Void> ping;

		public WaitRequest(List<Managers> managers, AsyncCallback<Void> ping) {
			this.managers = managers;
			this.ping = ping;
		}
	}

	private List<Managers> ready = new ArrayList<Managers>();
	private List<WaitRequest> waiting = new LinkedList<WaitRequest>();

	public void waitForManager(AsyncCallback<Void> ping, Managers... managers) {
		List<Managers> managersList = new ArrayList<Managers>();
		if (managers != null) {
			for (Managers m : managers) {
				managersList.add(m);
			}
		}

		managersList.removeAll(ready);
		if (managersList.isEmpty()) {
			ping.onSuccess(null);
		} else {
			waiting.add(new WaitRequest(managersList, ping));
		}
	}
}
