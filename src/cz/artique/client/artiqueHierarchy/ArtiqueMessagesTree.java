package cz.artique.client.artiqueHierarchy;

import cz.artique.client.artiqueHierarchy.MessageWidget.MessageWidgetFactory;
import cz.artique.client.manager.Managers;
import cz.artique.client.messages.Message;
import cz.artique.client.messages.MessagesManager;

public class ArtiqueMessagesTree
		extends AbstractHierarchyTree<Message, MessagesManager> {

	public ArtiqueMessagesTree() {
		super(Managers.MESSAGES_MANAGER, MessageWidgetFactory.FACTORY);
	}
	
	@Override
	protected void initialized() {
		expand(2);
	}

}
