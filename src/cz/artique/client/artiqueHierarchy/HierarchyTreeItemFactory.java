package cz.artique.client.artiqueHierarchy;

import cz.artique.client.hierarchy.Hierarchy;
import cz.artique.shared.utils.HasHierarchy;
import cz.artique.shared.utils.HasName;

public interface HierarchyTreeItemFactory<E extends HasName & HasHierarchy> {
	HierarchyTreeItem<E> createTreeItem(Hierarchy<E> hierarchy);
}
