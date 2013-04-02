package cz.artique.client.messages;

import com.google.gwt.event.shared.EventHandler;

public interface MessageHandler extends EventHandler {
	void onMessageAdded(MessageEvent e);
}
