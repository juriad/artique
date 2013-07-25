package cz.artique.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.i18n.I18n;
import cz.artique.client.manager.Managers;
import cz.artique.client.messages.Message;
import cz.artique.client.messages.MessageType;
import cz.artique.client.service.ClientPingService;
import cz.artique.client.service.ClientPingServiceAsync;
import cz.artique.shared.model.user.UserInfo;

public enum ArtiqueWorld {
	WORLD;

	private UserInfo userInfo;
	
	private ClientPingServiceAsync service = GWT
		.create(ClientPingService.class);
	private Timer timer;
	private boolean online = true;

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
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
