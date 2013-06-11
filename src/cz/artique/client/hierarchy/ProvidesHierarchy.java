package cz.artique.client.hierarchy;

import cz.artique.shared.utils.HasHierarchy;

public interface ProvidesHierarchy<E extends HasHierarchy> {
	Hierarchy<E> getHierarchyRoot();

	Hierarchy<E> getAdhocItem();
}
