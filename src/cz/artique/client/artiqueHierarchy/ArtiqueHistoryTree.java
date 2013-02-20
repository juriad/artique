package cz.artique.client.artiqueHierarchy;

import cz.artique.client.artiqueHistory.ArtiqueHistory;
import cz.artique.client.artiqueHistory.HistoryItem;
import cz.artique.client.hierarchy.HierarchyTreeWidgetFactory;

public class ArtiqueHistoryTree extends AbstractHierarchyTree<HistoryItem, ArtiqueHistory> {

	public ArtiqueHistoryTree(ArtiqueHistory manager,
			HierarchyTreeWidgetFactory<HistoryItem> factory) {
		super(ArtiqueHistory.HISTORY, factory);
	}

}
