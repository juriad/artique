package cz.artique.client.history;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.hierarchy.Hierarchy;
import cz.artique.client.hierarchy.HierarchyUtils;
import cz.artique.client.hierarchy.InnerNode;
import cz.artique.client.hierarchy.ProvidesHierarchy;
import cz.artique.client.hierarchy.TimedLeafNode;
import cz.artique.client.manager.Manager;
import cz.artique.client.manager.ManagerReady;
import cz.artique.client.manager.Managers;
import cz.artique.client.manager.Managers.ManagerInitCallback;
import cz.artique.client.utils.SortedList;
import cz.artique.shared.model.config.client.ClientConfigKey;
import cz.artique.shared.model.label.Filter;
import cz.artique.shared.model.label.ListFilter;
import cz.artique.shared.model.label.ListFilterOrder;

/**
 * Manages history state of application.
 * Provides methods which controls current state.
 * 
 * @author Adam Juraszek
 * 
 */
public class HistoryManager
		implements ProvidesHierarchy<HistoryItem>, HasHistoryHandlers, Manager {

	/**
	 * Restore {@link ListFilter} and add new {@link HistoryItem}.
	 * This is used to react to browser event: back, forward.
	 * 
	 * @author Adam Juraszek
	 * 
	 */
	public class ArtiqueHistoryHandler implements ValueChangeHandler<String> {

		public void onValueChange(ValueChangeEvent<String> event) {
			String token = event.getValue();
			HistoryItem historyItem = getByToken(token);
			if (historyItem == null) {
				ListFilter deserialized =
					HistoryUtils.UTILS.deserializeListFilter(event.getValue());
				historyItem = new HistoryItem(deserialized, token);
			}

			addHistoryItem(historyItem);
			CachingHistoryUtils.UTILS.setBaseListFilter();
		}
	}

	public static final HistoryManager HISTORY = new HistoryManager();

	/**
	 * Observes GWT history.
	 */
	private HistoryManager() {
		// observe GWT history
		History.addValueChangeHandler(new ArtiqueHistoryHandler());

		Managers.addManagerInitCallback(new ManagerInitCallback() {
			@Override
			public void initManager() {
				Managers.waitForManagers(new ManagerReady() {
					public void onReady() {
						setMaxItems(Managers.CONFIG_MANAGER
							.getConfig(ClientConfigKey.HISTORY_MAX_ITEMS)
							.get()
							.getI());
					}
				}, Managers.CONFIG_MANAGER);
			}
		});
	}

	// const, same as HISTORY_MAX_ITEMS
	private int maxItems = 100;

	private final LinkedList<HistoryItem> historyItems =
		new LinkedList<HistoryItem>();

	private Map<String, HistoryItem> tokenMap =
		new HashMap<String, HistoryItem>();

	/**
	 * Set {@link ListFilter} as a new {@link HistoryItem}.
	 * This is called from within the application.
	 * 
	 * @param listFilter
	 *            {@link ListFilter}
	 * @param token
	 *            serialized {@link ListFilter}
	 */
	public void setListFilter(ListFilter listFilter, String token) {
		tokenMap.put(token, new HistoryItem(listFilter, token));
		if (getLastHistoryItem() != null
			&& getLastHistoryItem().getToken().equals(token)) {
			// same token, but updated listfilter
			History.fireCurrentHistoryState();
		} else {
			// tokens are not same
			History.newItem(token);
		}
	}

	/**
	 * Actually adds new {@link HistoryItem} both to history and hierarchy.
	 * 
	 * @param historyItem
	 *            {@link HistoryItem} to add
	 */
	protected void addHistoryItem(HistoryItem historyItem) {
		if (historyItems.contains(historyItem)) {
			historyItems.remove(historyItem);
			HierarchyUtils.remove(hierarchyRoot, historyItem);
		}

		historyItems.add(historyItem);
		getHierarchyRoot().addChild(
			new TimedLeafNode<HistoryItem>(historyItem, getHierarchyRoot()));

		while (historyItems.size() > getMaxItems()) {
			HistoryItem first = historyItems.removeFirst();
			if (first != null) {
				HierarchyUtils.remove(hierarchyRoot, first);
			}
		}

		fireHistoryChanged();
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

	/**
	 * @return number of items to keep in hierarchy
	 */
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

	/**
	 * Clears hierarchy.
	 */
	public void clear() {
		while (historyItems.size() > 1) {
			HistoryItem removed = historyItems.remove(1);
			HierarchyUtils.remove(hierarchyRoot, removed);
		}
	}

	private final InnerNode<HistoryItem> hierarchyRoot = HierarchyUtils
		.createRootNode();

	public InnerNode<HistoryItem> getHierarchyRoot() {
		return hierarchyRoot;
	}

	/**
	 * @return {@link ListFilter} with no {@link Filter}; other criteria are
	 *         preserved.
	 */
	public ListFilter getBaseListFilter() {
		ListFilter lf = new ListFilter();
		if (historyItems.size() > 0) {
			ListFilter last = historyItems.getLast().getListFilter();
			lf.setOrder(last.getOrder());
			lf.setRead(last.getRead());
			lf.setEndTo(last.getEndTo());
			lf.setStartFrom(last.getStartFrom());
		} else {
			lf.setOrder(ListFilterOrder.getDefault());
			lf.setRead(null);
		}
		return lf;
	}

	/**
	 * @return current history item or null (in early part of initialization)
	 */
	public HistoryItem getLastHistoryItem() {
		return historyItems.isEmpty() ? null : historyItems.getLast();
	}

	/**
	 * Search for existing {@link HistoryItem} represented by token.
	 * 
	 * @param token
	 *            token
	 * @return {@link HistoryItem} or null if not found
	 */
	public HistoryItem getByToken(String token) {
		return tokenMap.get(token);
	}

	/**
	 * Extends handlers to prioritize their calls.
	 * 
	 * @author Adam Juraszek
	 * 
	 * @param <E>
	 *            type of handler
	 */
	private static class PriorityHandler<E>
			implements Comparable<PriorityHandler<E>> {
		private final E handler;
		private final int priority;

		public PriorityHandler(E handler, int priority) {
			this.handler = handler;
			this.priority = priority;
		}

		/**
		 * @return actual handler
		 */
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

	public void ready(ManagerReady ping) {
		ping.onReady();
	}

	public boolean isReady() {
		return true;
	}

	public Hierarchy<HistoryItem> getAdhocItem() {
		return null;
	}
}
