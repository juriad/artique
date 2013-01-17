package cz.artique.client.hierarchy;

import java.util.List;

import cz.artique.shared.utils.HasName;

public interface Hierarchy<E> extends HasName {
	E getItem();

	List<Hierarchy<E>> getChildren();

	Hierarchy<E> getParent();
}
