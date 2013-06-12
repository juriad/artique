package cz.artique.shared.utils;

import com.google.gwt.user.client.ui.Tree;

import cz.artique.client.hierarchy.Hierarchy;
import cz.artique.client.hierarchy.HierarchyUtils;

/**
 * Marker interface for classes which are intended to form a {@link Hierarchy}
 * structure displayed in {@link Tree} widget.
 * 
 * <p>
 * {@link HierarchyUtils} process list of Objects implementing
 * {@link HasHierarchy} and wraps them into {@link Hierarchy} objects. Tree
 * structure is derived from value returned {@link #getHierarchy()} by splitting
 * it by slash sign. Value returned by {@link #getHierarchy()} must therefore
 * list names if all ancestors of that object.
 * 
 * @author Adam Juraszek
 * 
 */
public interface HasHierarchy {
	/**
	 * @return hierarchy, path to the root
	 */
	String getHierarchy();

	/**
	 * @return name to be displayed for this item
	 */
	String getName();
}
