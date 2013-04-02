package cz.artique.client.messages;

import com.google.gwt.event.shared.HandlerRegistration;

public interface HasMessageHandlers {
	HandlerRegistration addMessageHandler(MessageHandler handler);
}
