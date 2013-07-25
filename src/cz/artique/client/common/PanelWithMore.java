package cz.artique.client.common;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * Panel contains sorted list of {@link Comparable} widgets; to the end an extra widget
 * may be added.
 * 
 * @author Adam Juraszek
 * 
 * @param <E>
 *            type of comparable widgets the panel contains
 */
public class PanelWithMore<E extends IsWidget & Comparable<E>>
		extends HoverPanel {
	private Widget extraWidget;

	/**
	 * Constructs the panel.
	 */
	public PanelWithMore() {
		super();
		addStyleName("panelWithMore");
	}

	/**
	 * Inserts a comparable widgets to place according to its value.
	 * 
	 * @param child
	 *            widget to add
	 */
	@SuppressWarnings("unchecked")
	public void insert(E child) {
		int index = 0;
		while (index < getWidgetCount() - (getExtraWidget() != null ? 1 : 0)
			&& child.compareTo((E) getWidget(index)) >= 0) {
			index++;
		}
		insert(child, index);
	}

	/**
	 * @return list of all comparable widgets
	 */
	@SuppressWarnings("unchecked")
	public List<E> getWidgets() {
		List<E> widgets = new ArrayList<E>();
		for (int i = 0; i < getWidgetCount()
			- (getExtraWidget() != null ? 1 : 0); i++) {
			widgets.add((E) getWidget(i));
		}
		return widgets;
	}

	/**
	 * Set to null to remove it at all.
	 * 
	 * @param extraWidget
	 *            extra widget shown at the end of panel
	 */
	public void setExtraWidget(Widget extraWidget) {
		if (getExtraWidget() != null) { // old
			remove(getExtraWidget());
		}
		this.extraWidget = extraWidget; // set
		if (getExtraWidget() != null) { // add
			add(getExtraWidget());
		}
	}

	/**
	 * @return extra widget shown at the end of panel
	 */
	public Widget getExtraWidget() {
		return extraWidget;
	}
}
