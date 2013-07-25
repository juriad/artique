package cz.artique.client.hierarchy;

import java.util.List;

import cz.artique.shared.utils.HasHierarchy;

/**
 * Represents hierarchical structure of nodes and leaves.
 * 
 * @author Adam Juraszek
 * 
 * @param <E>
 *            type of hierarchy
 */
public interface Hierarchy<E extends HasHierarchy>
		extends HasHierarchy, HasHierarchyChangeHandlers<E>,
		Comparable<Hierarchy<E>> {

	/**
	 * @return list of child nodes
	 */
	List<Hierarchy<E>> getChildren();

	/**
	 * @return list of sibling nodes
	 */
	List<Hierarchy<E>> getSiblings();

	/**
	 * @return index among sibling nodes
	 */
	int getIndex();

	/**
	 * @return parent node, null if root
	 */
	Hierarchy<E> getParent();

	/**
	 * Notify {@link HierarchyChangeHandler}s about change.
	 */
	void fireChanged();

	/**
	 * @param list
	 *            accumulator across recursion of hierarchical objects in
	 *            leaves
	 */
	void getAll(List<E> list);
}
