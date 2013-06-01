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
	
	@Override
	protected void initialized() {
		expand(2);
	}
}
