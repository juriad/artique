package cz.artique.client.messages;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

import cz.artique.client.manager.Managers;

public class Messenger extends Composite {
	private VerticalPanel vp;

	public Messenger() {
		vp = new VerticalPanel();
		initWidget(vp);
		setStylePrimaryName("messages");

		Managers.MESSAGES_MANAGER.addMessageHandler(new MessageHandler() {

			public void onMessageAdded(MessageEvent e) {
				if (e.getMessage().getMessageType().isNotification()) {
					vp.insert(new MessageBubbleWidget(e.getMessage()), 0);
				}
			}
		});
	}
}
