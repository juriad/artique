package cz.artique.client.hierarchy;

import com.google.gwt.user.client.ui.IsWidget;

import cz.artique.shared.utils.HasHierarchy;

/**
 * Widget representing pobject with hierarchy.
 * 
 * @author Adam Juraszek
 * 
 * @param <E>
 *            type of hierarchy
 */
public interface HierarchyTreeWidget<E extends HasHierarchy> extends IsWidget {
	/**
	 * Refreshes content of widget.
	 * 
	 * @return true if widget shall be shown in tree; false otherwise.
	 */
	boolean refresh();

	/**
	 * @return returns hierarchy object
	 */
	Hierarchy<E> getHierarchy();

	/**
	 * @param selected
	 *            whether is selected
	 */
	void setSelected(boolean selected);

	/**
	 * @return whether is selected
	 */
	boolean isSelected();

}
