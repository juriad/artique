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
import cz.artique.client.artiqueHistory.HistoryItem;
import cz.artique.client.hierarchy.Hierarchy;
import cz.artique.client.hierarchy.HierarchyTreeWidget;
import cz.artique.client.hierarchy.HierarchyTreeWidgetFactory;
import cz.artique.client.hierarchy.LeafNode;

public class HistoryWidget extends Composite
		implements HierarchyTreeWidget<HistoryItem> {

	public static class HistoryWidgetFactory
			implements HierarchyTreeWidgetFactory<HistoryItem> {

		public static final HierarchyTreeWidgetFactory<HistoryItem> FACTORY =
			new HistoryWidgetFactory();

		public HierarchyTreeWidget<HistoryItem> createWidget(
				Hierarchy<HistoryItem> hierarchy) {
			return new HistoryWidget(hierarchy);
		}
	}

	private Hierarchy<HistoryItem> hierarchy;

	public HistoryWidget(Hierarchy<HistoryItem> hierarchy) {
		this.hierarchy = hierarchy;

		if (hierarchy instanceof LeafNode) {
			LeafNode<HistoryItem> leaf = (LeafNode<HistoryItem>) hierarchy;
			final HistoryItem item = leaf.getItem();

			Anchor anchor =
				new Anchor(hierarchy.getName(), "#" + item.getToken());

			initWidget(anchor);

			anchor.addClickHandler(new ClickHandler() {
				final HyperlinkImpl impl = GWT.create(HyperlinkImpl.class);

				public void onClick(ClickEvent event) {
					if (impl.handleAsClick(Event.as(event.getNativeEvent()))) {

						ArtiqueHistory.HISTORY.setListFilter(
							item.getListFilter(), item.getToken());

						event.preventDefault();
					}
				}
			});
		} else {
			Label label = new Label("/");
			initWidget(label);
		}
	}

	public void refresh() {
		// do nothing
	}

	public Hierarchy<HistoryItem> getHierarchy() {
		return hierarchy;
	}

}
