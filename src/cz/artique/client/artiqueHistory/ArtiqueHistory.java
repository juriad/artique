package cz.artique.client.artiqueHistory;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.ArtiqueWorld;
import cz.artique.client.artiqueSources.ProvidesHierarchy;
import cz.artique.client.config.ArtiqueConfigManager;
import cz.artique.client.hierarchy.Hierarchy;
import cz.artique.client.hierarchy.HierarchyUtils;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.config.ClientConfigKey;
import cz.artique.shared.model.label.FilterOrder;
import cz.artique.shared.model.label.ListFilter;

public enum ArtiqueHistory implements ProvidesHierarchy<ListFilter> {
	HISTORY;

	private ArtiqueHistory() {
		ArtiqueWorld.WORLD.waitForManager(
			Arrays.asList(Managers.CONFIG_MANAGER), new AsyncCallback<Void>() {

				public void onSuccess(Void result) {
					setMaxItems(ArtiqueConfigManager.MANAGER.getConfig(
						ClientConfigKey.HISTORY_MAX_ITEMS).<Integer> get());
				}

				public void onFailure(Throwable caught) {
					// ignore
				}
			});
	}

	// const, same as HISTORY_MAX_ITEMS
	private int maxItems = 30;

	private final Deque<ListFilter> historyFilters =
		new LinkedList<ListFilter>();

	public void addListFilter(ListFilter listFilter, String token) {
		if (historyFilters.isEmpty()) {
			add(listFilter, token);
		} else {
			ListFilter last = historyFilters.getLast();
			if (!last.equalsDeeply(listFilter)) {
				add(listFilter, token);
			}
		}

		while (historyFilters.size() > getMaxItems()) {
			ListFilter first = historyFilters.removeFirst();
			if (first != null) {
				HierarchyUtils.remove(hierarchyRoot, first);
			}
		}
	}

	private void add(ListFilter listFilter, String token) {
		historyFilters.add(listFilter);
		History.newItem(token);
	}

	public int getMaxItems() {
		return maxItems;
	}

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
			ListFilter last = historyFilters.getLast();
			lf.setOrder(last.getOrder());
			lf.setRead(last.getRead());
		} else {
			lf.setOrder(FilterOrder.getDefault());
			lf.setRead(null);
		}
		return lf;
	}
}
