package cz.artique.client.hierarchy;

import com.google.gwt.event.shared.HandlerRegistration;

import cz.artique.shared.utils.HasHierarchy;

public interface HasHierarchyChangeHandlers<E extends HasHierarchy> {
	HandlerRegistration addHierarchyChangeHandler(
			HierarchyChangeHandler<E> handler);
}
