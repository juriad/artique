package cz.artique.client.hierarchy;

import cz.artique.shared.utils.HasHierarchy;

public interface HierarchyTreeWidgetFactory<E extends HasHierarchy> {
	HierarchyTreeWidget<E> createWidget(Hierarchy<E> hierarchy);

}
