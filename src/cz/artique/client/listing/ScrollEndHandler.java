package cz.artique.client.listing;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler called when scrollbar position gets to the top or bottom of the
 * {@link InfiniteList} or is near the top or bottom.
 * 
 * @author Adam Juraszek
 * 
 */
public interface ScrollEndHandler extends EventHandler {
	/**
	 * Called whenever {@link ScrollEndEvent} occurs.
	 * 
	 * @param event
	 *            {@link ScrollEndEvent}
	 */
	void onScrollEnd(ScrollEndEvent event);
}
