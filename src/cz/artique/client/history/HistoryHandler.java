package cz.artique.client.history;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler called when history is changed.
 * 
 * @author Adam Juraszek
 * 
 */
public interface HistoryHandler extends EventHandler {
	/**
	 * Called whenever {@link HistoryEvent} occurs.
	 * 
	 * @param e
	 *            {@link HistoryEvent}
	 */
	void onHistoryChanged(HistoryEvent e);
}
