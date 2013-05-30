package cz.artique.client.common;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class PanelWithMore<E extends IsWidget & Comparable<E>>
		extends FlowPanel {
	private boolean expanded;
	private Widget extraWidget;

	public PanelWithMore() {
		super();
		setStylePrimaryName("panelWithMore");
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
		setStyleDependentName("expanded", expanded);
	}

	public boolean isExpanded() {
		return expanded;
	}

	@SuppressWarnings("unchecked")
	public void insert(E child) {
		int index = 0;
		while (index < getWidgetCount() - (getExtraWidget() != null ? 1 : 0)
			&& child.compareTo((E) getWidget(index)) >= 0) {
			index++;
		}
		insert(child, index);
	}

	@SuppressWarnings("unchecked")
	public List<E> getWidgets() {
		List<E> widgets = new ArrayList<E>();
		for (int i = 0; i < getWidgetCount()
			- (getExtraWidget() != null ? 1 : 0); i++) {
			widgets.add((E) getWidget(i));
		}
		return widgets;
	}

	public void setExtraWidget(Widget extraWidget) {
		if (extraWidget != null) {
			remove(extraWidget);
		}
		add(extraWidget);
	}

	public Widget getExtraWidget() {
		return extraWidget;
	}
}
