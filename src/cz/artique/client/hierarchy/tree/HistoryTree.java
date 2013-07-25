package cz.artique.client.hierarchy.tree;

import cz.artique.client.hierarchy.HierarchyChangeEvent;
import cz.artique.client.hierarchy.tree.HistoryWidget.HistoryWidgetFactory;
import cz.artique.client.history.HistoryItem;
import cz.artique.client.history.HistoryManager;

/**
 * Not used.
 * 
 * @author Adam Juraszek
 * 
 */
public class HistoryTree
		extends AbstractHierarchyTree<HistoryItem, HistoryManager> {

	public HistoryTree() {
		super(HistoryManager.HISTORY, HistoryWidgetFactory.FACTORY);
	}

	private int lastCount = 0;

	/**
	 * Expand two levels.
	 * 
	 * @see cz.artique.client.hierarchy.tree.AbstractHierarchyTree#afterHierarchyChange(cz.artique.client.hierarchy.HierarchyChangeEvent)
	 */
	@Override
	protected void afterHierarchyChange(HierarchyChangeEvent<HistoryItem> event) {
		if (getRootItem().getChildCount() > 0 && lastCount < 1) {
			expand(2);
			lastCount = getRootItem().getChildCount();
		}
	}

}
