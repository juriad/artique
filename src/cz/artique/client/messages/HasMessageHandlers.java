package cz.artique.client.messages;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Marks classes which can have {@link MessageHandler}s.
 * 
 * @author Adam Juraszek
 * 
 */
public interface HasMessageHandlers {
	/**
	 * Add new handler which will be called when {@link MessageEvent} occurs.
	 * 
	 * @param handler
	 *            handler
	 * @return registration
	 */
	HandlerRegistration addMessageHandler(MessageHandler handler);
}
