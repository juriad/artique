package cz.artique.client.hierarchy;

import com.google.gwt.user.client.ui.TreeItem;

import cz.artique.shared.utils.HasHierarchy;
import cz.artique.shared.utils.HasName;

public class HierarchyTreeItem<E extends HasHierarchy & HasName>
		extends TreeItem {

	private final Hierarchy<E> hierarchy;

	public HierarchyTreeItem(Hierarchy<E> hierarchy) {
		this.hierarchy = hierarchy;
	}

	public Hierarchy<E> getHierarchy() {
		return hierarchy;
	}

	public void refresh() {
		// refresh if change event occured
	}

}
