package cz.artique.client.artiqueSources;

import cz.artique.client.hierarchy.Hierarchy;
import cz.artique.shared.utils.HasHierarchy;
import cz.artique.shared.utils.HasName;

public interface ProvidesHierarchy<E extends HasName & HasHierarchy> {
	Hierarchy<E> getHierarchyRoot();
}
