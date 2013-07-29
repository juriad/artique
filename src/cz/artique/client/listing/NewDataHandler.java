package cz.artique.client.listing;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler called when new data in {@link InfiniteList} are available or has
 * been shown.
 * 
 * @author Adam Juraszek
 * 
 */
public interface NewDataHandler extends EventHandler {
	/**
	 * Called whenever {@link NewDataEvent} occurs.
	 * 
	 * @param event
	 *            {@link NewDataEvent}
	 */
	void onNewData(NewDataEvent event);
}
