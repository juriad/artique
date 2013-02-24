package cz.artique.client.artiqueHierarchy;

import cz.artique.client.artiqueHierarchy.HistoryWidget.HistoryWidgetFactory;
import cz.artique.client.artiqueHistory.ArtiqueHistory;
import cz.artique.client.artiqueHistory.HistoryItem;

public class ArtiqueHistoryTree
		extends AbstractHierarchyTree<HistoryItem, ArtiqueHistory> {

	public ArtiqueHistoryTree() {
		super(ArtiqueHistory.HISTORY, HistoryWidgetFactory.FACTORY);
	}

}
