package cz.artique.client.messages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.user.client.ui.VerticalPanel;

import cz.artique.client.manager.Managers;

public class Messenger extends VerticalPanel {
	interface MyResources extends ClientBundle {
		@NotStrict
		@Source("Messenger.css")
		CssResource style();
	}

	private static final MyResources res = GWT.create(MyResources.class);

	public Messenger() {
		res.style().ensureInjected();
		setStylePrimaryName("messages");

		Managers.MESSAGES_MANAGER.addMessageHandler(new MessageHandler() {
			public void onMessageAdded(MessageEvent e) {
				if (e.getMessage().getMessageType().isNotification()) {
					insert(new MessageWidget(e.getMessage()), 0);
				}
			}
		});
	}
}
