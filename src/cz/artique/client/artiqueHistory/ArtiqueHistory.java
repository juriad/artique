package cz.artique.client.artiqueHistory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.ArtiqueWorld;
import cz.artique.client.config.ArtiqueConfigManager;
import cz.artique.client.hierarchy.Hierarchy;
import cz.artique.client.hierarchy.HierarchyUtils;
import cz.artique.client.hierarchy.ProvidesHierarchy;
import cz.artique.client.manager.Manager;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.config.ClientConfigKey;
import cz.artique.shared.model.label.FilterOrder;
import cz.artique.shared.model.label.ListFilter;

public class ArtiqueHistory
		implements ProvidesHierarchy<HistoryItem>, HasHistoryHandlers, Manager {
	public static final ArtiqueHistory HISTORY = new ArtiqueHistory();

	private ArtiqueHistory() {
		ArtiqueWorld.WORLD.waitForManager(new AsyncCallback<Void>() {

			public void onSuccess(Void result) {
				setMaxItems(ArtiqueConfigManager.MANAGER
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
		boolean added = false;
		if (historyItems.isEmpty()) {
			add(listFilter, token, issueEvent);
			added = true;
		} else {
			String last = historyItems.getLast().getToken();
			if (!last.equals(token)) {
				HistoryItem existing = getByToken(token);
				if (existing != null) {
					historyItems.remove(existing);
					HierarchyUtils.remove(hierarchyRoot, existing);
				}
				add(listFilter, token, issueEvent);
				added = true;
			}
		}

		while (historyItems.size() > getMaxItems()) {
			HistoryItem first = historyItems.removeFirst();
			if (first != null) {
				HierarchyUtils.remove(hierarchyRoot, first);
			}
		}

		if (!issueEvent && added) {
			fireHistoryChanged();
		}
	}

	/**
	 * Called by this class at the end of addListFilter if not issueEvent
	 */
	private void fireHistoryChanged() {
		HistoryEvent event = new HistoryEvent();
		for (HistoryHandler hh : handlers) {
			hh.onHistoryChanged(event);
		}
	}

	public void addListFilter(ListFilter listFilter, String token) {
		addListFilter(listFilter, token, true);
	}

	private void add(ListFilter listFilter, String token, boolean issueEvent) {
		historyItems.add(new HistoryItem(listFilter, token));
		System.out.println("new item");
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

	private final Hierarchy<HistoryItem> hierarchyRoot = HierarchyUtils
		.createRootNode();

	public Hierarchy<HistoryItem> getHierarchyRoot() {
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
			lf.setOrder(FilterOrder.getDefault());
			lf.setRead(null);
		}
		return lf;
	}

	public HistoryItem getLastHistoryItem() {
		return historyItems.isEmpty() ? null : historyItems.getLast();
	}

	public HistoryItem getByToken(String token) {
		for (int i = historyItems.size() - 1; i >= 0; i--) {
			HistoryItem historyItem = historyItems.get(i);
			if (historyItem.getToken().equals(token)) {
				return historyItem;
			}
		}
		return null;
	}

	List<HistoryHandler> handlers = new ArrayList<HistoryHandler>();

	public HandlerRegistration addHistoryHandler(final HistoryHandler handler) {
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

	public void ready(AsyncCallback<Void> ping) {
		ping.onSuccess(null);
	}

	public boolean isReady() {
		return true;
	}
}
