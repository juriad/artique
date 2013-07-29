package cz.artique.client.listing;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

/**
 * Marks classes which can have {@link NewDataHandler}.
 * 
 * @author Adam Juraszek
 * 
 */
public interface HasNewDataHandlers extends HasHandlers {
	/**
	 * Add new handler which will be called when {@link NewDataEvent} occurs.
	 * 
	 * @param handler
	 *            handler
	 * @return registration
	 */
	HandlerRegistration addNewDataHandler(NewDataHandler handler);
}
