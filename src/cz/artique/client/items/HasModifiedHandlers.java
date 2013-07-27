package cz.artique.client.items;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Marks classes which can have {@link ModifiedHandler}.
 * 
 * @author Adam Juraszek
 * 
 */
public interface HasModifiedHandlers {
	/**
	 * Add new handler which will be called when {@link ModifiedEvent} occurs.
	 * 
	 * @param handler
	 *            handler
	 * @return registration
	 */
	HandlerRegistration addModifiedHandler(ModifiedHandler handler);
}
