package cz.artique.client.hierarchy;

import cz.artique.shared.utils.HasHierarchy;

/**
 * Marks classes which can provide their content in hierarchical manner.
 * 
 * @author Adam Juraszek
 * 
 * @param <E>
 *            type of hierarchy
 */
public interface ProvidesHierarchy<E extends HasHierarchy> {
	/**
	 * @return root of {@link Hierarchy}
	 */
	Hierarchy<E> getHierarchyRoot();

	/**
	 * @return adhoc hierarchy node (somehow special)
	 */
	Hierarchy<E> getAdhocItem();
}
