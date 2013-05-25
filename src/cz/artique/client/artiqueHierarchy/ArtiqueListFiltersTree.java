package cz.artique.client.artiqueHierarchy;

import java.util.Arrays;

import com.google.gwt.user.client.ui.TreeItem;

import cz.artique.client.artiqueHierarchy.ListFilterWidget.ListFilterWidgetFactory;
import cz.artique.client.artiqueHistory.ArtiqueHistory;
import cz.artique.client.artiqueHistory.HistoryEvent;
import cz.artique.client.artiqueHistory.HistoryHandler;
import cz.artique.client.artiqueHistory.HistoryItem;
import cz.artique.client.artiqueListFilters.ArtiqueListFiltersManager;
import cz.artique.client.hierarchy.Hierarchy;
import cz.artique.client.hierarchy.HierarchyTreeWidget;
import cz.artique.client.hierarchy.HierarchyUtils;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.label.ListFilter;

public class ArtiqueListFiltersTree
		extends AbstractHierarchyTree<ListFilter, ArtiqueListFiltersManager> {

	public ArtiqueListFiltersTree() {
		super(Managers.LIST_FILTERS_MANAGER, ListFilterWidgetFactory.FACTORY);
	}

	@Override
	protected void initialized() {
		observeHistoryChange();
		expand(2);
	}

	private void observeHistoryChange() {
		ArtiqueHistory.HISTORY.addHistoryHandler(new HistoryHandler() {
			@SuppressWarnings("unchecked")
			public void onHistoryChanged(HistoryEvent e) {
				HistoryItem historyItem =
					ArtiqueHistory.HISTORY.getLastHistoryItem();
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
