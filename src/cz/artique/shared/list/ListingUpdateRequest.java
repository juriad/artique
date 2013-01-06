package cz.artique.shared.list;

import java.io.Serializable;
import java.util.Date;

import com.google.appengine.api.datastore.Key;

import cz.artique.shared.model.label.Filter;

public class ListingUpdateRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	private Filter filter;
	private Key firstKey;
	private Key lastKey;
	private int fetchCount;
	private Boolean read;
	private Date lastFetch;

	public ListingUpdateRequest() {}

	public ListingUpdateRequest(Filter filter, Key firstKey, Key lastKey,
			int fetchCount, Boolean read, Date lastFetch) {
		this.setFilter(filter);
		this.setRead(read);
		this.setFirstKey(firstKey);
		this.setLastKey(lastKey);
		this.setFetchCount(fetchCount);
		this.setLastFetch(lastFetch);
	}

	public Key getFirstKey() {
		return firstKey;
	}

	public Key getLastKey() {
		return lastKey;
	}

	public int getFetchCount() {
		return fetchCount;
	}

	public Filter getFilter() {
		return filter;
	}

	public Boolean getRead() {
		return read;
	}

	public Date getLastFetch() {
		return lastFetch;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	public void setFirstKey(Key firstKey) {
		this.firstKey = firstKey;
	}

	public void setLastKey(Key lastKey) {
		this.lastKey = lastKey;
	}

	public void setFetchCount(int fetchCount) {
		this.fetchCount = fetchCount;
	}

	public void setRead(Boolean read) {
		this.read = read;
	}

	public void setLastFetch(Date lastFetch) {
		this.lastFetch = lastFetch;
	}
}
