package cz.artique.client.items;

import com.google.gwt.event.shared.EventHandler;

import cz.artique.shared.model.item.UserItem;

/**
 * Handler called when some {@link UserItem}s are changed.
 * 
 * @author Adam Juraszek
 * 
 */
public interface ModifiedHandler extends EventHandler {
	/**
	 * Called whenever {@link ModifiedEvent} occures.
	 * 
	 * @param e
	 *            {@link ModifiedEvent}
	 */
	void onModified(ModifiedEvent e);
}
