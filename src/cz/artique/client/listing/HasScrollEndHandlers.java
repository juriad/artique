package cz.artique.client.listing;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

/**
 * Marks classes which can have {@link ScrollEndHandler}s.
 * 
 * @author Adam Juraszek
 * 
 */
public interface HasScrollEndHandlers extends HasHandlers {
	/**
	 * Add new handler which will be called when {@link ScrollEndEvent} occurs.
	 * 
	 * @param handler
	 *            handler
	 * @return registration
	 */
	HandlerRegistration addScrollEndHandler(ScrollEndHandler handler);
}
