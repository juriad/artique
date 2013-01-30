package cz.artique.client.artiqueHistory;

import java.util.LinkedList;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.ArtiqueWorld;
import cz.artique.client.config.ArtiqueConfigManager;
import cz.artique.client.hierarchy.Hierarchy;
import cz.artique.client.hierarchy.HierarchyUtils;
import cz.artique.client.hierarchy.ProvidesHierarchy;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.config.ClientConfigKey;
import cz.artique.shared.model.label.FilterOrder;
import cz.artique.shared.model.label.ListFilter;

public class ArtiqueHistory implements ProvidesHierarchy<ListFilter> {
	public static final ArtiqueHistory HISTORY = new ArtiqueHistory();

	private class HistoryItem {
		ListFilter listFilter;
		String token;

		public HistoryItem(ListFilter listFilter, String token) {
			this.listFilter = listFilter;
			this.token = token;
		}
	}

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

	private final LinkedList<HistoryItem> historyFilters =
		new LinkedList<HistoryItem>();

	public void addListFilter(ListFilter listFilter, String token,
			boolean issueEvent) {
		if (historyFilters.isEmpty()) {
			add(listFilter, token, issueEvent);
		} else {
			String last = historyFilters.getLast().token;
			if (!last.equals(token)) {
				add(listFilter, token, issueEvent);
			}
		}

		while (historyFilters.size() > getMaxItems()) {
			ListFilter first = historyFilters.removeFirst().listFilter;
			if (first != null) {
				HierarchyUtils.remove(hierarchyRoot, first);
			}
		}
	}

	public void addListFilter(ListFilter listFilter, String token) {
		addListFilter(listFilter, token, true);
	}

	private void add(ListFilter listFilter, String token, boolean issueEvent) {
		historyFilters.add(new HistoryItem(listFilter, token));
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

	private final Hierarchy<ListFilter> hierarchyRoot = HierarchyUtils
		.createRootNode();

	public Hierarchy<ListFilter> getHierarchyRoot() {
		return hierarchyRoot;
	}

	public ListFilter getBaseListFilter() {
		ListFilter lf = new ListFilter();
		lf.setUser(ArtiqueWorld.WORLD.getUser());
		if (historyFilters.size() > 0) {
			ListFilter last = historyFilters.getLast().listFilter;
			lf.setOrder(last.getOrder());
			lf.setRead(last.getRead());
		} else {
			lf.setOrder(FilterOrder.getDefault());
			lf.setRead(null);
		}
		return lf;
	}

	public ListFilter getLastListFilter() {
		return historyFilters.isEmpty()
			? null
			: historyFilters.getLast().listFilter;
	}

	public String getLastToken() {
		return historyFilters.isEmpty() ? null : historyFilters.getLast().token;
	}

	public ListFilter getByToken(String token) {
		for (int i = historyFilters.size() - 1; i >= 0; i--) {
			HistoryItem historyItem = historyFilters.get(i);
			if (historyItem.token.equals(token)) {
				return historyItem.listFilter;
			}
		}
		return null;
	}
}
