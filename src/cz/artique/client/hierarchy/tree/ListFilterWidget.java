package cz.artique.client.hierarchy.tree;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
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

/**
 * Widget representing {@link ListFilter} in {@link ListFiltersTree}.
 * 
 * @author Adam Juraszek
 * 
 */
public class ListFilterWidget extends AbstractHierarchyTreeWidget<ListFilter> {

	/**
	 * Factory.
	 * 
	 * @author Adam Juraszek
	 * 
	 */
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

	/**
	 * Constructs widget depending on whether it is root, inner, leaf or ad-hoc
	 * node.
	 * 
	 * @param hierarchy
	 *            hierarchy node
	 */
	public ListFilterWidget(final Hierarchy<ListFilter> hierarchy) {
		super(hierarchy);

		if (hierarchy instanceof LeafNode) {
			LeafNode<ListFilter> leaf = (LeafNode<ListFilter>) hierarchy;
			if (leaf.getItem().getKey() == null) {
				createAdhocPanel();
			} else {
				createLeafNodePanel();
			}
		} else if (hierarchy instanceof InnerNode) {
			if (hierarchy.getParent() != null) {
				createInnerNodePanel();
			} else {
				createRootPanel();
			}
		}
	}

	/**
	 * Create content in case it is root node.
	 */
	private void createRootPanel() {
		String clearTooltip =
			I18n.getHierarchyTreeConstants().clearListFilterTooltip();
		createAnchor(I18n.getHierarchyTreeConstants().listFilterRootText(),
			null, clearHandler, clearTooltip);
		createImage(HierarchyResources.INSTANCE.clear(), clearHandler,
			clearTooltip);
		addGeneralInnerImages();
	}

	/**
	 * Create content in case it is inner node.
	 */
	private void createInnerNodePanel() {
		createLabel(getHierarchy().getName(), null, null);
		addGeneralInnerImages();
	}

	/**
	 * Add icons shared among root and inner nodes.
	 */
	private void addGeneralInnerImages() {
		String createNewTooltip =
			I18n.getHierarchyTreeConstants().createNewListFilterTooltip();
		createImage(HierarchyResources.INSTANCE.createNew(), createNewHandler,
			createNewTooltip);
		String saveCurrentTooltip =
			I18n.getHierarchyTreeConstants().saveCurrentListFilterTooltip();
		createImage(HierarchyResources.INSTANCE.saveCurrent(),
			saveCurrentHandler, saveCurrentTooltip);
	}

	/**
	 * Create content in case it is leaf node.
	 */
	private void createLeafNodePanel() {
		LeafNode<ListFilter> leaf = (LeafNode<ListFilter>) getHierarchy();
		final ListFilter item = leaf.getItem();
		final String token = HistoryUtils.UTILS.serializeListFilter(item);
		createAnchor(getHierarchy().getName(), token, new ClickHandler() {
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
		createImage(HierarchyResources.INSTANCE.detail(), new ClickHandler() {
			public void onClick(ClickEvent event) {
				ListFilterDialog.DIALOG.showDialog(item);
			}
		}, detailTooltip);
	}

	/**
	 * Create content in case it is ad-hoc node.
	 */
	private void createAdhocPanel() {
		String adhoc = I18n.getHierarchyTreeConstants().adhoc();
		String adhocTooltip = I18n.getHierarchyTreeConstants().adhocTooltip();
		createAnchor(adhoc, null, new ClickHandler() {
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
		createImage(HierarchyResources.INSTANCE.clear(), clearHandler,
			clearTooltip);
	}
}
