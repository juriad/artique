package cz.artique.client.hierarchy;

import com.google.gwt.event.shared.EventHandler;

import cz.artique.shared.utils.HasHierarchy;
import cz.artique.shared.utils.HasName;

public interface HierarchyChangeHandler<E extends HasHierarchy & HasName>
		extends EventHandler {
	void onHierarchyChange(HierarchyChangeEvent<E> event);
}
