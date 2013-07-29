package cz.artique.client.shortcuts;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Marks classes which can have {@link ShortcutHandler}s.
 * 
 * @author Adam Juraszek
 * 
 */
public interface HasShortcutHandlers {
	/**
	 * Add new handler which will be called when {@link ShortcutEvent} occurs.
	 * 
	 * @param handler
	 *            handler
	 * @return registration
	 */
	HandlerRegistration addShortcutHandler(ShortcutHandler handler);
}
