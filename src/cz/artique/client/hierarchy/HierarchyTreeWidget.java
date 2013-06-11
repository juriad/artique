package cz.artique.client.hierarchy;

import com.google.gwt.user.client.ui.IsWidget;

import cz.artique.shared.utils.HasHierarchy;

public interface HierarchyTreeWidget<E extends HasHierarchy>
		extends IsWidget {
	boolean refresh();

	Hierarchy<E> getHierarchy();

	void setSelected(boolean selected);

	boolean isSelected();

}
