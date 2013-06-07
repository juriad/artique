package cz.artique.client.hierarchy.tree;

import cz.artique.client.hierarchy.HierarchyChangeEvent;
import cz.artique.client.hierarchy.tree.HistoryWidget.HistoryWidgetFactory;
import cz.artique.client.history.HistoryItem;
import cz.artique.client.history.HistoryManager;

public class HistoryTree
		extends AbstractHierarchyTree<HistoryItem, HistoryManager> {

	public HistoryTree() {
		super(HistoryManager.HISTORY, HistoryWidgetFactory.FACTORY);
	}

	private int lastCount = 0;

	@Override
	protected void afterUpdate(HierarchyChangeEvent<HistoryItem> event) {
		if (getRootItem().getChildCount() > 0 && lastCount < 1) {
			expand(2);
			lastCount = getRootItem().getChildCount();
		}
	}

}
