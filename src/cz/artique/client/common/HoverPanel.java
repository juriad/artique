package cz.artique.client.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.InsertPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class HoverPanel extends ComplexPanel implements InsertPanel.ForIsWidget {

	interface MyResources extends ClientBundle {
		@NotStrict
		@Source("HoverPanel.css")
		CssResource style();
	}

	private static final MyResources res = GWT.create(MyResources.class);

	private boolean expanded;
	private final Element inner;

	public HoverPanel() {
		res.style().ensureInjected();
		Element outer = DOM.createDiv();
		setElement(outer);
		setStylePrimaryName("hoverPanel");

		inner = DOM.createDiv();
		DOM.appendChild(outer, inner);

		setStyleName(inner, "hoverPanelInner", true);
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
		setStyleDependentName("expanded", expanded);
	}

	public boolean isExpanded() {
		return expanded;
	}

	@Override
	public void setWidth(String width) {
		super.setWidth(width);
		DOM.setStyleAttribute(inner, "width", width);
	}

	@Override
	public void add(Widget w) {
		insert(w, getWidgetCount());
	}

	public void insert(Widget w, int beforeIndex) {
		insert(w, inner, beforeIndex, true);
	}

	public void insert(IsWidget w, int beforeIndex) {
		insert(asWidgetOrNull(w), beforeIndex);
	}
}
