package cz.artique.client.hierarchy;

import com.google.gwt.user.client.ui.IsWidget;

import cz.artique.shared.utils.HasHierarchy;
import cz.artique.shared.utils.HasName;

public interface HierarchyTreeWidget<E extends HasHierarchy & HasName>
		extends IsWidget {
	void refresh();

	Hierarchy<E> getHierarchy();

}
