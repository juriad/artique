package cz.artique.client.artiquHistory;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.History;

import cz.artique.client.artiqueSources.ProvidesHierarchy;
import cz.artique.client.hierarchy.Hierarchy;
import cz.artique.client.hierarchy.HierarchyUtils;
import cz.artique.shared.model.label.ListFilter;

public enum ArtiqueHistory implements ProvidesHierarchy<ListFilter> {
	HISTORY;

	private int maxItems;

	private final List<ListFilter> historyFilters =
		new LinkedList<ListFilter>();

	public void addListFilter(ListFilter listFilter) {
		if (historyFilters.isEmpty()) {
			add(listFilter);
		} else {
			ListFilter last = historyFilters.get(historyFilters.size() - 1);
			if (!last.equalsDeeply(listFilter)) {
				add(listFilter);
			}
		}

		if (historyFilters.size() > getMaxItems()) {
			List<ListFilter> subList =
				historyFilters
					.subList(0, historyFilters.size() - getMaxItems());
			for (ListFilter lf : subList) {
				HierarchyUtils.remove(hierarchyRoot, lf);
			}
			subList.clear();
		}
	}

	private void add(ListFilter listFilter) {
		historyFilters.add(listFilter);
		History.newItem(HistoryUtils.serializeListFilter(listFilter));
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
}
