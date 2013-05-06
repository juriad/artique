package cz.artique.client.artiqueHierarchy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.impl.HyperlinkImpl;

import cz.artique.client.ArtiqueWorld;
import cz.artique.client.artiqueHistory.ArtiqueHistory;
import cz.artique.client.artiqueHistory.HistoryItem;
import cz.artique.client.hierarchy.Hierarchy;
import cz.artique.client.hierarchy.HierarchyTreeWidget;
import cz.artique.client.hierarchy.HierarchyTreeWidgetFactory;
import cz.artique.client.hierarchy.InnerNode;
import cz.artique.client.hierarchy.LeafNode;
import cz.artique.client.i18n.ArtiqueI18n;

public class HistoryWidget extends AbstractHierarchyTreeWidget<HistoryItem> {

	public static class HistoryWidgetFactory
			implements HierarchyTreeWidgetFactory<HistoryItem> {

		public static final HierarchyTreeWidgetFactory<HistoryItem> FACTORY =
			new HistoryWidgetFactory();

		public HierarchyTreeWidget<HistoryItem> createWidget(
				Hierarchy<HistoryItem> hierarchy) {
			return new HistoryWidget(hierarchy);
		}
	}

	private static ClickHandler clearHandler = new ClickHandler() {
		public void onClick(ClickEvent event) {
			ArtiqueHistory.HISTORY.clear();
		}
	};

	public HistoryWidget(final Hierarchy<HistoryItem> hierarchy) {
		super(hierarchy);

		FlowPanel panel = new FlowPanel();
		initWidget(panel);

		if (hierarchy instanceof LeafNode) {
			createLeafNodePanel(panel);
		} else if (hierarchy instanceof InnerNode) {
			if (hierarchy.getParent() != null) {
				createInnerNodePanel(panel);
			} else {
				createRootPanel(panel);
			}
		}
	}

	private void createRootPanel(FlowPanel panel) {
		String clearTooltip =
			ArtiqueI18n.I18N.getConstants().clearHistoryTooltip();
		createLabel(panel, "/", null, null);
		createImage(panel, ArtiqueWorld.WORLD.getResources().clear(),
			clearHandler, clearTooltip);
	}

	// unnecessary
	private void createInnerNodePanel(FlowPanel panel) {
		createLabel(panel, getHierarchy().getName(), null, null);
	}

	private void createLeafNodePanel(FlowPanel panel) {
		LeafNode<HistoryItem> leaf = (LeafNode<HistoryItem>) getHierarchy();
		final HistoryItem item = leaf.getItem();
		final String token = item.getToken();
		createAnchor(panel, getHierarchy().getName(), token,
			new ClickHandler() {
				final HyperlinkImpl impl = GWT.create(HyperlinkImpl.class);

				public void onClick(ClickEvent event) {
					if (impl.handleAsClick(Event.as(event.getNativeEvent()))) {
						ArtiqueHistory.HISTORY.setListFilter(
							item.getListFilter(), token);
						event.preventDefault();
					}
				}
			}, null);
		// TODO tooltip
	}
}
