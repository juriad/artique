package cz.artique.shared.items;

import java.io.Serializable;
import java.util.Date;

import com.google.appengine.api.datastore.Key;

import cz.artique.shared.model.label.ListFilter;

public class ListingUpdateRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	private ListFilter listFilter;
	private Key firstKey;
	private Key lastKey;
	private Date lastFetch;
	private int fetchCount;

	public ListingUpdateRequest() {}

	public ListingUpdateRequest(ListFilter listFilter, Key firstKey,
			Key lastKey, Date lastFetch, int fetchCount) {
		this.setListFilter(listFilter);
		this.setFirstKey(firstKey);
		this.setLastKey(lastKey);
		this.setLastFetch(lastFetch);
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

	public Date getLastFetch() {
		return lastFetch;
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

	public void setLastFetch(Date lastFetch) {
		this.lastFetch = lastFetch;
	}

	public int getFetchCount() {
		return fetchCount;
	}

	public void setFetchCount(int fetchCount) {
		this.fetchCount = fetchCount;
	}
}
