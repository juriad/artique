package cz.artique.client.utils;

import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class InlineFlowPanel extends ComplexPanel {
	/**
	 * Creates an empty inline flow panel.
	 */
	public InlineFlowPanel() {
		setElement(DOM.createSpan());
	}

	/**
	 * Adds a new child widget to the panel.
	 * 
	 * @param w
	 *            the widget to be added
	 */
	@Override
	public void add(Widget w) {
		add(w, getElement());
	}

	@Override
	public void clear() {
		// Remove all existing child nodes.
		Node child = getElement().getFirstChild();
		while (child != null) {
			getElement().removeChild(child);
			child = getElement().getFirstChild();
		}
	}

	public void insert(IsWidget w, int beforeIndex) {
		insert(asWidgetOrNull(w), beforeIndex);
	}

	/**
	 * Inserts a widget before the specified index.
	 * 
	 * @param w
	 *            the widget to be inserted
	 * @param beforeIndex
	 *            the index before which it will be inserted
	 * @throws IndexOutOfBoundsException
	 *             if <code>beforeIndex</code> is out of
	 *             range
	 */
	public void insert(Widget w, int beforeIndex) {
		insert(w, getElement(), beforeIndex, true);
	}
}
