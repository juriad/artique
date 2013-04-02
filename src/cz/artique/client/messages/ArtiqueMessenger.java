package cz.artique.client.messages;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

import cz.artique.client.manager.Managers;

public class ArtiqueMessenger extends Composite {
	private VerticalPanel vp;

	public ArtiqueMessenger() {
		vp = new VerticalPanel();
		initWidget(vp);
		vp.getElement().setAttribute("position", "fixed");
		vp.getElement().setAttribute("width", "100%");
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
