package cz.artique.client.hierarchy;

import cz.artique.shared.utils.HasHierarchy;
import cz.artique.shared.utils.HasName;

public interface ProvidesHierarchy<E extends HasName & HasHierarchy> {
	Hierarchy<E> getHierarchyRoot();
}
