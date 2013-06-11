package cz.artique.client.history;

import cz.artique.shared.model.label.ListFilter;
import cz.artique.shared.utils.HasHierarchy;

public class HistoryItem implements HasHierarchy {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HistoryItem other = (HistoryItem) obj;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		return true;
	}
}
