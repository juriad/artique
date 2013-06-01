package cz.artique.client;

import com.google.appengine.api.users.User;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.artiqueListing.ArtiqueList;
import cz.artique.client.hierarchy.tree.SourcesTree;
import cz.artique.client.i18n.I18n;
import cz.artique.client.manager.Managers;
import cz.artique.client.messages.Message;
import cz.artique.client.messages.MessageType;
import cz.artique.client.service.ClientPingService;
import cz.artique.client.service.ClientPingServiceAsync;

public enum ArtiqueWorld {
	WORLD;

	private ClientPingServiceAsync service = GWT
		.create(ClientPingService.class);

	private Timer timer;

	private UserInfo userInfo;
	private ArtiqueList list;
	private Resources resources;
	private SourcesTree sourcesTree;
	private boolean online = true;

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

	public void setResources(Resources resources) {
		this.resources = resources;
	}

	public Resources getResources() {
		return resources;
	}

	public SourcesTree getSourcesTree() {
		return sourcesTree;
	}

	public void setSourcesTree(SourcesTree sourcesTree) {
		this.sourcesTree = sourcesTree;
	}

	public boolean isOnline() {
		return online;
	}

	protected void setOnline(boolean online) {
		if (online != this.online) {
			ArtiqueConstants constants = I18n.getArtiqueConstants();
			Message message;
			if (online) {
				message = new Message(MessageType.INFO, constants.gotOnline());
			} else {
				message =
					new Message(MessageType.FAILURE, constants.gotOffline());
			}
			Managers.MESSAGES_MANAGER.addMessage(message, true);
		}
		this.online = online;
	}

	public void testOnline() {
		if (timer != null) {
			return;
		}
		service.ping(new AsyncCallback<Void>() {
			public void onSuccess(Void result) {
				setOnline(true);
			}

			public void onFailure(Throwable caught) {
				setOnline(false);
				timer = new Timer() {
					@Override
					public void run() {
						service.ping(new AsyncCallback<Void>() {

							public void onFailure(Throwable caught) {
								// continue testing
							}

							public void onSuccess(Void result) {
								setOnline(true);
								timer.cancel();
								timer = null;
							}
						});
					}
				};
				timer.scheduleRepeating(10000);
			}
		});
	}

}
