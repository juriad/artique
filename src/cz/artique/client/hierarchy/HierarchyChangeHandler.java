package cz.artique.client.hierarchy;

import com.google.gwt.event.shared.EventHandler;

import cz.artique.shared.utils.HasHierarchy;

/**
 * Handler called when hierarchy is changed (added, removed or renamed node).
 * 
 * @author Adam Juraszek
 * 
 * @param <E>
 *            type of hierarchy
 */
public interface HierarchyChangeHandler<E extends HasHierarchy>
		extends EventHandler {
	/**
	 * Called when hierarchy is changed.
	 * 
	 * @param event
	 *            {@link HierarchyChangeEvent} describing what and what manner
	 *            changed
	 */
	void onHierarchyChange(HierarchyChangeEvent<E> event);
}
