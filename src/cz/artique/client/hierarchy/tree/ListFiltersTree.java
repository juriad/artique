package cz.artique.client.hierarchy.tree;

import java.util.Arrays;

import com.google.gwt.user.client.ui.TreeItem;

import cz.artique.client.hierarchy.Hierarchy;
import cz.artique.client.hierarchy.HierarchyTreeWidget;
import cz.artique.client.hierarchy.HierarchyUtils;
import cz.artique.client.hierarchy.tree.ListFilterWidget.ListFilterWidgetFactory;
import cz.artique.client.history.HistoryEvent;
import cz.artique.client.history.HistoryHandler;
import cz.artique.client.history.HistoryItem;
import cz.artique.client.history.HistoryManager;
import cz.artique.client.listFilters.ListFiltersManager;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.label.ListFilter;

public class ListFiltersTree
		extends AbstractHierarchyTree<ListFilter, ListFiltersManager> {

	public ListFiltersTree() {
		super(Managers.LIST_FILTERS_MANAGER, ListFilterWidgetFactory.FACTORY);
	}

	@Override
	protected void initialized() {
		observeHistoryChange();
		expand(2);
	}

	private void observeHistoryChange() {
		HistoryManager.HISTORY.addHistoryHandler(new HistoryHandler() {
			@SuppressWarnings("unchecked")
			public void onHistoryChanged(HistoryEvent e) {
				HistoryItem historyItem =
					HistoryManager.HISTORY.getLastHistoryItem();
				if (historyItem != null) {
					ListFilter listFilter = historyItem.getListFilter();
					if (listFilter.getKey() != null) {
						Hierarchy<ListFilter> hierarchy =
							HierarchyUtils.findInTree(getRoot(), listFilter);
						if (hierarchy != null) {
							TreeItem inTree =
								findInTree(hierarchy, getRootItem());
							if (inTree != null) {
								select(Arrays
									.asList((HierarchyTreeWidget<ListFilter>) inTree
										.getWidget()));
								return;
							}
						}
					}
				}
				select(Arrays
					.asList((HierarchyTreeWidget<ListFilter>) getAdhocTreeItem()
						.getWidget()));
			}
		});
	}
}
