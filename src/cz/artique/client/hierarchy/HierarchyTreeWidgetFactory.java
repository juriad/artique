package cz.artique.client.hierarchy;

import cz.artique.shared.utils.HasHierarchy;

/**
 * Factory of hierarchy widgets.
 * 
 * @author Adam Juraszek
 * 
 * @param <E>
 *            type of hierarchy
 */
public interface HierarchyTreeWidgetFactory<E extends HasHierarchy> {
	/**
	 * Creates hierarchy widgets based on hierarchy object.
	 * 
	 * @param hierarchy
	 *            hierarchy object
	 * @return hierarchy widget
	 */
	HierarchyTreeWidget<E> createWidget(Hierarchy<E> hierarchy);

}
