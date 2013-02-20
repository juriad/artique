package cz.artique.client.artiqueHistory;

import cz.artique.shared.model.label.ListFilter;
import cz.artique.shared.utils.HasHierarchy;
import cz.artique.shared.utils.HasName;

public class HistoryItem implements HasHierarchy, HasName {
	private final ListFilter listFilter;
	private final String token;

	public HistoryItem(ListFilter listFilter, String token) {
		this.listFilter = listFilter;
		this.token = token;
	}

	public ListFilter getListFilter() {
		return listFilter;
	}

	public String getToken() {
		return token;
	}

	public String getName() {
		return token;
	}

	public String getHierarchy() {
		return "/";
	}

	public void setHierarchy(String hierarchy) {}
}
