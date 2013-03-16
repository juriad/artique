package cz.artique.client.artiqueHierarchy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.impl.HyperlinkImpl;

import cz.artique.client.artiqueHistory.ArtiqueHistory;
import cz.artique.client.artiqueHistory.HistoryUtils;
import cz.artique.client.hierarchy.Hierarchy;
import cz.artique.client.hierarchy.HierarchyTreeWidget;
import cz.artique.client.hierarchy.HierarchyTreeWidgetFactory;
import cz.artique.client.hierarchy.LeafNode;
import cz.artique.shared.model.label.ListFilter;

public class ListFilterWidget extends Composite
		implements HierarchyTreeWidget<ListFilter> {

	public static class ListFilterWidgetFactory
			implements HierarchyTreeWidgetFactory<ListFilter> {

		public static final HierarchyTreeWidgetFactory<ListFilter> FACTORY =
			new ListFilterWidgetFactory();

		public HierarchyTreeWidget<ListFilter> createWidget(
				Hierarchy<ListFilter> hierarchy) {
			return new ListFilterWidget(hierarchy);
		}
	}

	public ListFilterWidget(Hierarchy<ListFilter> hierarchy) {
		this.hierarchy = hierarchy;

		if (hierarchy instanceof LeafNode) {
			LeafNode<ListFilter> leaf = (LeafNode<ListFilter>) hierarchy;
			final ListFilter item = leaf.getItem();

			final String token = HistoryUtils.UTILS.serializeListFilter(item);

			Anchor anchor = new Anchor(hierarchy.getName(), "#" + token);

			initWidget(anchor);

			anchor.addClickHandler(new ClickHandler() {
				final HyperlinkImpl impl = GWT.create(HyperlinkImpl.class);

				public void onClick(ClickEvent event) {
					if (impl.handleAsClick(Event.as(event.getNativeEvent()))) {

						ArtiqueHistory.HISTORY.addListFilter(item, token);

						event.preventDefault();
					}
				}
			});
		} else {
			Label label = new Label(hierarchy.getName());
			initWidget(label);
		}
	}

	private Hierarchy<ListFilter> hierarchy;

	public void refresh() {
		// do nothing
	}

	public Hierarchy<ListFilter> getHierarchy() {
		return hierarchy;
	}

}
