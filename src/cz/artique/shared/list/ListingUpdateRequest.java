package cz.artique.shared.list;

import java.util.Date;

import com.google.appengine.api.datastore.Key;

import cz.artique.shared.model.label.Filter;

public class ListingUpdateRequest {
	private final Filter filter;
	private final Key firstKey;
	private final Key lastKey;
	private final int fetchCount;
	private final Boolean read;
	private final Date lastFetch;

	public ListingUpdateRequest(Filter filter, Key firstKey, Key lastKey,
			int fetchCount, Boolean read, Date lastFetch) {
		this.filter = filter;
		this.read = read;
		this.firstKey = firstKey;
		this.lastKey = lastKey;
		this.fetchCount = fetchCount;
		this.lastFetch = lastFetch;
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
}
