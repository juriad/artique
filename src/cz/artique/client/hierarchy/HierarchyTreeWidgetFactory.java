package cz.artique.client.hierarchy;

import cz.artique.shared.utils.HasHierarchy;
import cz.artique.shared.utils.HasName;

public interface HierarchyTreeWidgetFactory<E extends HasHierarchy & HasName> {
	HierarchyTreeWidget<E> createWidget(Hierarchy<E> hierarchy);

}
