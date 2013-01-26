package cz.artique.client.hierarchy;

import com.google.gwt.event.shared.HandlerRegistration;

import cz.artique.shared.utils.HasHierarchy;
import cz.artique.shared.utils.HasName;

public interface HasHierarchyChangeHandlers<E extends HasHierarchy & HasName> {
	HandlerRegistration addHierarchyChangeHandler(
			HierarchyChangeHandler<E> handler);
}
