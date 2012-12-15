package cz.artique.shared.list;

import java.util.Date;

import com.google.appengine.api.datastore.Key;

import cz.artique.shared.model.label.Filter;

public class ListingUpdateRequest {
	private final Filter filter;
	private final Date lastFetch;
	private final Key firstKey;
	private final Key lastKey;
	private final int fetchCount;

	public ListingUpdateRequest(Filter filter, Date lastFetch, Key firstKey,
			Key lastKey, int fetchCount) {
		this.filter = filter;
		this.lastFetch = lastFetch;
		this.firstKey = firstKey;
		this.lastKey = lastKey;
		this.fetchCount = fetchCount;
	}

	public Date getLastFetch() {
		return lastFetch;
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
}
