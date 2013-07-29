package cz.artique.client.messages;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler called when there exists a {@link Message} which shall be processed.
 * 
 * @author Adam Juraszek
 * 
 */
public interface MessageHandler extends EventHandler {
	/**
	 * Called whenever {@link MessageEvent} occurs.
	 * 
	 * @param e
	 *            {@link MessageEvent}
	 */
	void onMessageAdded(MessageEvent e);
}
