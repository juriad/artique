package cz.artique.client.hierarchy;

import com.google.gwt.event.shared.HandlerRegistration;

import cz.artique.shared.utils.HasHierarchy;

/**
 * Marks classes which can have hierarchy change handlers.
 * Only {@link Hierarchy} currently does so
 * 
 * @author Adam Juraszek
 * 
 * @param <E>
 *            type of hierarchy
 */
public interface HasHierarchyChangeHandlers<E extends HasHierarchy> {
	/**
	 * Adds handler to list of handlers notified when an
	 * {@link HierarchyChangeEvent} occures.
	 * 
	 * @param handler
	 *            handler to register
	 * @return registration
	 */
	HandlerRegistration addHierarchyChangeHandler(
			HierarchyChangeHandler<E> handler);
}
