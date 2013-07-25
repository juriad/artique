package cz.artique.client.history;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Marks classes which can have {@link HistoryHandler}.
 * 
 * @author Adam Juraszek
 * 
 */
public interface HasHistoryHandlers {
	/**
	 * Add new handler with default (zero) priority which will be called when
	 * {@link HistoryEvent} occurs.
	 * 
	 * @param handler
	 *            handler
	 * @return registration
	 */
	HandlerRegistration addHistoryHandler(HistoryHandler handler);

	/**
	 * Add new handler with user-defined priority which will be called when
	 * {@link HistoryEvent} occurs.
	 * 
	 * @param handler
	 *            handler
	 * @param priority
	 *            priority
	 * @return registration
	 */
	HandlerRegistration addHistoryHandler(HistoryHandler handler, int priority);
}
