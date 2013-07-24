package cz.artique.client.hierarchy.tree;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.impl.HyperlinkImpl;

import cz.artique.client.hierarchy.Hierarchy;
import cz.artique.client.hierarchy.HierarchyTreeWidget;
import cz.artique.client.hierarchy.HierarchyTreeWidgetFactory;
import cz.artique.client.hierarchy.InnerNode;
import cz.artique.client.hierarchy.LeafNode;
import cz.artique.client.history.CachingHistoryUtils;
import cz.artique.client.history.HistoryItem;
import cz.artique.client.history.HistoryManager;
import cz.artique.client.history.HistoryUtils;
import cz.artique.client.i18n.I18n;
import cz.artique.client.listFilters.AdhocDialog;
import cz.artique.client.listFilters.ListFilterDialog;
import cz.artique.shared.model.label.ListFilter;

public class ListFilterWidget extends AbstractHierarchyTreeWidget<ListFilter> {

	public static class ListFilterWidgetFactory
			implements HierarchyTreeWidgetFactory<ListFilter> {

		public static final HierarchyTreeWidgetFactory<ListFilter> FACTORY =
			new ListFilterWidgetFactory();

		public HierarchyTreeWidget<ListFilter> createWidget(
				Hierarchy<ListFilter> hierarchy) {
			return new ListFilterWidget(hierarchy);
		}
	}

	private static ClickHandler clearHandler = new ClickHandler() {
		public void onClick(ClickEvent event) {
			ListFilter baseListFilter =
				HistoryManager.HISTORY.getBaseListFilter();
			String token =
				CachingHistoryUtils.UTILS.serializeListFilter(baseListFilter);
			HistoryManager.HISTORY.setListFilter(baseListFilter, token);
		}
	};

	private ClickHandler createNewHandler = new ClickHandler() {
		public void onClick(ClickEvent event) {
			ListFilter baseListFilter =
				HistoryManager.HISTORY.getBaseListFilter();
			baseListFilter.setHierarchy(getHierarchy().getHierarchy());
			ListFilterDialog.DIALOG.showDialog(baseListFilter);
		}
	};

	private ClickHandler saveCurrentHandler = new ClickHandler() {
		public void onClick(ClickEvent event) {
			HistoryItem lastHistoryItem =
				HistoryManager.HISTORY.getLastHistoryItem();
			final ListFilter lf;
			if (lastHistoryItem == null) {
				lf = HistoryManager.HISTORY.getBaseListFilter();
			} else {
				lf = lastHistoryItem.getListFilter().clone();
			}
			lf.setHierarchy(getHierarchy().getHierarchy());
			ListFilterDialog.DIALOG.showDialog(lf);
		}
	};

	public ListFilterWidget(final Hierarchy<ListFilter> hierarchy) {
		super(hierarchy);

		if (hierarchy instanceof LeafNode) {
			LeafNode<ListFilter> leaf = (LeafNode<ListFilter>) hierarchy;
			if (leaf.getItem().getKey() == null) {
				createAdhocPanel(getPanel());
			} else {
				createLeafNodePanel(getPanel());
			}
		} else if (hierarchy instanceof InnerNode) {
			if (hierarchy.getParent() != null) {
				createInnerNodePanel(getPanel());
			} else {
				createRootPanel(getPanel());
			}
		}
	}

	private void createRootPanel(FlowPanel panel) {
		String clearTooltip =
			I18n.getHierarchyTreeConstants().clearListFilterTooltip();
		createAnchor(panel, I18n
			.getHierarchyTreeConstants()
			.listFilterRootText(), null, clearHandler, clearTooltip);
		createImage(panel, HierarchyResources.INSTANCE.clear(), clearHandler,
			clearTooltip);
		addGeneralInnerImages(panel);
	}

	private void createInnerNodePanel(FlowPanel panel) {
		createLabel(panel, getHierarchy().getName(), null, null);
		addGeneralInnerImages(panel);
	}

	private void addGeneralInnerImages(FlowPanel panel) {
		String createNewTooltip =
			I18n.getHierarchyTreeConstants().createNewListFilterTooltip();
		createImage(panel, HierarchyResources.INSTANCE.createNew(),
			createNewHandler, createNewTooltip);
		String saveCurrentTooltip =
			I18n.getHierarchyTreeConstants().saveCurrentListFilterTooltip();
		createImage(panel, HierarchyResources.INSTANCE.saveCurrent(),
			saveCurrentHandler, saveCurrentTooltip);
	}

	private void createLeafNodePanel(FlowPanel panel) {
		LeafNode<ListFilter> leaf = (LeafNode<ListFilter>) getHierarchy();
		final ListFilter item = leaf.getItem();
		final String token = HistoryUtils.UTILS.serializeListFilter(item);
		createAnchor(panel, getHierarchy().getName(), token,
			new ClickHandler() {
				final HyperlinkImpl impl = GWT.create(HyperlinkImpl.class);

				public void onClick(ClickEvent event) {
					if (impl.handleAsClick(Event.as(event.getNativeEvent()))) {
						HistoryManager.HISTORY.setListFilter(item, token);
						event.preventDefault();
					}
				}
			}, null);

		String detailTooltip =
			I18n.getHierarchyTreeConstants().detailListFilterTooltip();
		createImage(panel, HierarchyResources.INSTANCE.detail(),
			new ClickHandler() {
				public void onClick(ClickEvent event) {
					ListFilterDialog.DIALOG.showDialog(item);
				}
			}, detailTooltip);
	}

	private void createAdhocPanel(FlowPanel panel) {
		String adhoc = I18n.getHierarchyTreeConstants().adhoc();
		String adhocTooltip = I18n.getHierarchyTreeConstants().adhocTooltip();
		createAnchor(panel, adhoc, null, new ClickHandler() {
			public void onClick(ClickEvent event) {
				HistoryItem lastHistoryItem =
					HistoryManager.HISTORY.getLastHistoryItem();
				final ListFilter lf;
				if (lastHistoryItem == null) {
					lf = HistoryManager.HISTORY.getBaseListFilter();
				} else {
					lf = lastHistoryItem.getListFilter().clone();
				}
				AdhocDialog.DIALOG.showDialog(lf);
			}
		}, adhocTooltip);
		String clearTooltip =
			I18n.getHierarchyTreeConstants().clearListFilterTooltip();
		createImage(panel, HierarchyResources.INSTANCE.clear(), clearHandler,
			clearTooltip);
	}
}
