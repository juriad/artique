package cz.artique.client.hierarchy.tree;

import cz.artique.client.hierarchy.tree.HistoryWidget.HistoryWidgetFactory;
import cz.artique.client.history.HistoryItem;
import cz.artique.client.history.HistoryManager;

public class HistoryTree
		extends AbstractHierarchyTree<HistoryItem, HistoryManager> {

	public HistoryTree() {
		super(HistoryManager.HISTORY, HistoryWidgetFactory.FACTORY);
	}

	@Override
	protected void initialized() {
		expand(2);
	}

}
