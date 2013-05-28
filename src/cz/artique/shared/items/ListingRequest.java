package cz.artique.shared.items;

import java.io.Serializable;

import com.google.appengine.api.datastore.Key;

import cz.artique.shared.model.label.ListFilter;

public class ListingRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	private ListFilter listFilter;
	private Key firstKey;
	private Key lastKey;
	private int fetchCount;

	public ListingRequest() {}

	public ListingRequest(ListFilter listFilter, Key firstKey, Key lastKey,
			int fetchCount) {
		this.setListFilter(listFilter);
		this.setFirstKey(firstKey);
		this.setLastKey(lastKey);
		this.setFetchCount(fetchCount);
	}

	public Key getFirstKey() {
		return firstKey;
	}

	public Key getLastKey() {
		return lastKey;
	}

	public ListFilter getListFilter() {
		return listFilter;
	}

	public void setListFilter(ListFilter listFilter) {
		this.listFilter = listFilter;
	}

	public void setFirstKey(Key firstKey) {
		this.firstKey = firstKey;
	}

	public void setLastKey(Key lastKey) {
		this.lastKey = lastKey;
	}

	public int getFetchCount() {
		return fetchCount;
	}

	public void setFetchCount(int fetchCount) {
		this.fetchCount = fetchCount;
	}
}
