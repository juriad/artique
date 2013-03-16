package cz.artique.client.artiqueHierarchy;

import com.google.gwt.user.client.ui.TreeItem;

import cz.artique.client.artiqueHierarchy.ListFilterWidget.ListFilterWidgetFactory;
import cz.artique.client.artiqueHistory.ArtiqueHistory;
import cz.artique.client.artiqueHistory.HistoryEvent;
import cz.artique.client.artiqueHistory.HistoryHandler;
import cz.artique.client.artiqueHistory.HistoryItem;
import cz.artique.client.artiqueListFilters.ArtiqueListFiltersManager;
import cz.artique.client.hierarchy.Hierarchy;
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
	}

	private void observeHistoryChange() {
		ArtiqueHistory.HISTORY.addHistoryHandler(new HistoryHandler() {
			public void onHistoryChanged(HistoryEvent e) {
				HistoryItem historyItem =
					ArtiqueHistory.HISTORY.getLastHistoryItem();
				if (historyItem != null) {
					ListFilter listFilter = historyItem.getListFilter();
					if (listFilter.getName() != null) {
						Hierarchy<ListFilter> hierarchy =
							HierarchyUtils.findInTree(getRoot(), listFilter);
						TreeItem inTree = findInTree(hierarchy, getRootItem());
						if (inTree != null) {
							getTree().setSelectedItem(inTree);
						} else {
							getTree().setSelectedItem(null);
						}
					} else {
						getTree().setSelectedItem(null);
					}
				}
			}
		});
	}

}
