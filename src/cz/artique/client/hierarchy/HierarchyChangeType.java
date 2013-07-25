package cz.artique.client.hierarchy;

/**
 * Type of {@link HierarchyChangeEvent} cause.
 * 
 * @author Adam Juraszek
 * 
 */
public enum HierarchyChangeType {
	/**
	 * A node has been added.
	 */
	ADDED,
	/**
	 * A node has been removed.
	 */
	REMOVED,
	/**
	 * A node changed its name.
	 */
	CHANGED;
}
