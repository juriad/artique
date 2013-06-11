package cz.artique.client.hierarchy;

import com.google.gwt.event.shared.EventHandler;

import cz.artique.shared.utils.HasHierarchy;

public interface HierarchyChangeHandler<E extends HasHierarchy>
		extends EventHandler {
	void onHierarchyChange(HierarchyChangeEvent<E> event);
}
