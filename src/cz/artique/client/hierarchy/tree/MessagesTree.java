package cz.artique.client.hierarchy.tree;

import cz.artique.client.hierarchy.tree.MessageWidget.MessageWidgetFactory;
import cz.artique.client.manager.Managers;
import cz.artique.client.messages.Message;
import cz.artique.client.messages.MessagesManager;

public class MessagesTree
		extends AbstractHierarchyTree<Message, MessagesManager> {

	public MessagesTree() {
		super(Managers.MESSAGES_MANAGER, MessageWidgetFactory.FACTORY);
	}

	private int lastCount = 0;

	@Override
	protected void afterUpdate() {
		if (getRootItem().getChildCount() > 0 && lastCount < 1) {
			expand(2);
			lastCount = getRootItem().getChildCount();
		}
	}
}
