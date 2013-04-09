package cz.artique.client.artiqueHistory;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.ArtiqueWorld;
import cz.artique.client.hierarchy.HierarchyUtils;
import cz.artique.client.hierarchy.InnerNode;
import cz.artique.client.hierarchy.ProvidesHierarchy;
import cz.artique.client.hierarchy.TimedLeafNode;
import cz.artique.client.manager.Manager;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.config.ClientConfigKey;
import cz.artique.shared.model.label.ListFilter;
import cz.artique.shared.model.label.ListFilterOrder;
import cz.artique.shared.utils.SortedList;

public class ArtiqueHistory
		implements ProvidesHierarchy<HistoryItem>, HasHistoryHandlers, Manager {
	public static final ArtiqueHistory HISTORY = new ArtiqueHistory();

	private ArtiqueHistory() {
		Managers.waitForManagers(new AsyncCallback<Void>() {

			public void onSuccess(Void result) {
				setMaxItems(Managers.CONFIG_MANAGER
					.getConfig(ClientConfigKey.HISTORY_MAX_ITEMS)
					.get()
					.getI());
			}

			public void onFailure(Throwable caught) {
				// ignore
			}
		}, Managers.CONFIG_MANAGER);
	}

	// const, same as HISTORY_MAX_ITEMS
	private int maxItems = 100;

	private final LinkedList<HistoryItem> historyItems =
		new LinkedList<HistoryItem>();

	public void addListFilter(ListFilter listFilter, String token,
			boolean issueEvent) {
		if (historyItems.isEmpty()) {
			add(listFilter, token, issueEvent);
		} else {
			// String last = historyItems.getLast().getToken();
			// if (!last.equals(token)) {
			HistoryItem existing = getByToken(token);
			if (existing != null) {
				historyItems.remove(existing);
				HierarchyUtils.remove(hierarchyRoot, existing);
			}
			add(listFilter, token, issueEvent);
			// }
		}

		while (historyItems.size() > getMaxItems()) {
			HistoryItem first = historyItems.removeFirst();
			if (first != null) {
				HierarchyUtils.remove(hierarchyRoot, first);
			}
		}

		if (!issueEvent) {
			fireHistoryChanged();
		}
	}

	/**
	 * Called by this class at the end of addListFilter if not issueEvent
	 */
	private void fireHistoryChanged() {
		HistoryEvent event = new HistoryEvent();
		for (int i = 0; i < handlers.size(); i++) {
			PriorityHandler<HistoryHandler> hh = handlers.get(i);
			hh.getHandler().onHistoryChanged(event);
		}
	}

	public void addListFilter(ListFilter listFilter, String token) {
		addListFilter(listFilter, token, true);
	}

	private void add(ListFilter listFilter, String token, boolean issueEvent) {
		HistoryItem historyItem = new HistoryItem(listFilter, token);
		historyItems.add(historyItem);

		getHierarchyRoot().addChild(
			new TimedLeafNode<HistoryItem>(historyItem, getHierarchyRoot()));

		History.newItem(token, issueEvent);
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

	private final InnerNode<HistoryItem> hierarchyRoot = HierarchyUtils
		.createRootNode();

	public InnerNode<HistoryItem> getHierarchyRoot() {
		return hierarchyRoot;
	}

	public ListFilter getBaseListFilter() {
		ListFilter lf = new ListFilter();
		lf.setUser(ArtiqueWorld.WORLD.getUser());
		if (historyItems.size() > 0) {
			ListFilter last = historyItems.getLast().getListFilter();
			lf.setOrder(last.getOrder());
			lf.setRead(last.getRead());
		} else {
			lf.setOrder(ListFilterOrder.getDefault());
			lf.setRead(null);
		}
		return lf;
	}

	public HistoryItem getLastHistoryItem() {
		return historyItems.isEmpty() ? null : historyItems.getLast();
	}

	public HistoryItem getByToken(String token) {
		for (HistoryItem hi : historyItems) {
			if (hi.getToken().equals(token)) {
				return hi;
			}
		}
		return null;
	}

	private static class PriorityHandler<E>
			implements Comparable<PriorityHandler<E>> {
		private final E handler;
		private final int priority;

		public PriorityHandler(E handler, int priority) {
			this.handler = handler;
			this.priority = priority;
		}

		public E getHandler() {
			return handler;
		}

		public int compareTo(PriorityHandler<E> o) {
			return -Integer.valueOf(priority).compareTo(o.priority);
		}
	}

	List<PriorityHandler<HistoryHandler>> handlers =
		new SortedList<PriorityHandler<HistoryHandler>>();

	public HandlerRegistration addHistoryHandler(HistoryHandler handler) {
		return addHistoryHandler(handler, 0);
	}

	public HandlerRegistration addHistoryHandler(HistoryHandler handler,
			int priority) {
		final PriorityHandler<HistoryHandler> priorityHandler =
			new PriorityHandler<HistoryHandler>(handler, priority);
		handlers.add(priorityHandler);
		return new HandlerRegistration() {

			public void removeHandler() {
				handlers.remove(priorityHandler);
			}
		};
	}

	/* trivial implementation of manager: */

	public void refresh(AsyncCallback<Void> ping) {}

	public void setTimeout(int timeout) {}

	public int getTimeout() {
		return 0;
	}

	public void ready(AsyncCallback<Void> ping) {
		ping.onSuccess(null);
	}

	public boolean isReady() {
		return true;
	}
}
