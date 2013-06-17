package cz.artique.client.messages;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.hierarchy.Hierarchy;
import cz.artique.client.hierarchy.HierarchyUtils;
import cz.artique.client.hierarchy.InnerNode;
import cz.artique.client.hierarchy.ProvidesHierarchy;
import cz.artique.client.hierarchy.TimedLeafNode;
import cz.artique.client.manager.Manager;
import cz.artique.client.manager.ManagerReady;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.config.client.ClientConfigKey;

public class MessagesManager
		implements ProvidesHierarchy<Message>, HasMessageHandlers, Manager {
	public static final MessagesManager MESSENGER = new MessagesManager();

	private MessagesManager() {
		Managers.waitForManagers(new ManagerReady() {

			public void onReady() {
				setMaxItems(Managers.CONFIG_MANAGER
					.getConfig(ClientConfigKey.MESSENGER_MAX_ITEMS)
					.get()
					.getI());
			}
		}, Managers.CONFIG_MANAGER);
	}

	// const, same as MESSENGER_MAX_ITEMS
	private int maxItems = 100;

	private final LinkedList<Message> messages = new LinkedList<Message>();

	public void addMessage(Message message, boolean addToMessagesList) {
		if (addToMessagesList) {
			messages.add(message);
			getHierarchyRoot().addChild(
				new TimedLeafNode<Message>(message, getHierarchyRoot()));

			while (messages.size() > getMaxItems()) {
				Message first = messages.removeFirst();
				if (first != null) {
					HierarchyUtils.remove(hierarchyRoot, first);
				}
			}
		}

		fireMessageAdded(new MessageEvent(message));
	}

	/**
	 * Called by this class at the end of addListFilter if not issueEvent
	 */
	private void fireMessageAdded(MessageEvent event) {
		for (int i = 0; i < handlers.size(); i++) {
			MessageHandler messageHandler = handlers.get(i);
			messageHandler.onMessageAdded(event);
		}
	}

	public int getMaxItems() {
		return maxItems;
	}

	/**
	 * Never may be zero or negative.
	 * 
	 * @param maxItems
	 */
	public void setMaxItems(int maxItems) {
		this.maxItems = maxItems;
	}

	private final InnerNode<Message> hierarchyRoot = HierarchyUtils
		.createRootNode();

	public InnerNode<Message> getHierarchyRoot() {
		return hierarchyRoot;
	}

	List<MessageHandler> handlers = new ArrayList<MessageHandler>();

	public HandlerRegistration addMessageHandler(final MessageHandler handler) {
		handlers.add(handler);
		return new HandlerRegistration() {
			public void removeHandler() {
				handlers.remove(handler);
			}
		};
	}

	/* trivial implementation of manager: */

	public void refresh(AsyncCallback<Void> ping) {}

	public void setTimeout(int timeout) {}

	public int getTimeout() {
		return 0;
	}

	public void ready(ManagerReady ping) {
		ping.onReady();
	}

	public boolean isReady() {
		return true;
	}

	public Hierarchy<Message> getAdhocItem() {
		return null;
	}

	public void clear() {
		while (messages.size() > 0) {
			Message removed = messages.remove(0);
			HierarchyUtils.remove(hierarchyRoot, removed);
		}
	}
}
