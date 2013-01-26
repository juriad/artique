package cz.artique.client.hierarchy;

import java.util.List;

import cz.artique.shared.utils.HasHierarchy;
import cz.artique.shared.utils.HasName;

public interface Hierarchy<E extends HasHierarchy & HasName>
		extends HasName, HasHierarchyChangeHandlers<E>,
		Comparable<Hierarchy<E>> {

	List<Hierarchy<E>> getChildren();

	Hierarchy<E> getParent();

	void fireChanged();
}
