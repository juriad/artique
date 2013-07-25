package cz.artique.client.hierarchy.tree;

import cz.artique.client.hierarchy.HierarchyChangeEvent;
import cz.artique.client.hierarchy.tree.MessageWidget.MessageWidgetFactory;
import cz.artique.client.manager.Managers;
import cz.artique.client.messages.Message;
import cz.artique.client.messages.MessagesManager;

/**
 * Tree containing pseudo-hierarchy of {@link Message}s.
 * 
 * @author Adam Juraszek
 * 
 */
public class MessagesTree
		extends AbstractHierarchyTree<Message, MessagesManager> {

	public MessagesTree() {
		super(Managers.MESSAGES_MANAGER, MessageWidgetFactory.FACTORY);
	}

	private int lastCount = 0;

	/**
	 * Expand two levels (maximum).
	 * 
	 * @see cz.artique.client.hierarchy.tree.AbstractHierarchyTree#afterHierarchyChange(cz.artique.client.hierarchy.HierarchyChangeEvent)
	 */
	@Override
	protected void afterHierarchyChange(HierarchyChangeEvent<Message> event) {
		if (getRootItem().getChildCount() > 0 && lastCount < 1) {
			expand(2);
			lastCount = getRootItem().getChildCount();
		}
	}
}
