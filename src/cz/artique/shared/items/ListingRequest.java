package cz.artique.shared.items;

import java.io.Serializable;

import com.google.appengine.api.datastore.Key;

import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.ListFilter;

/**
 * Request specifying what {@link UserItem}s client wants.
 * It contains {@link ListFilter} which filters desired {@link UserItem}s, first
 * and last key the client possesses (null if this the first request) and number
 * of {@link UserItem}s client asks for.
 * 
 * @author Adam Juraszek
 * @see ListingResponse
 * 
 */
public class ListingRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	private ListFilter listFilter;
	private Key firstKey;
	private Key lastKey;
	private int fetchCount;

	/**
	 * Default constructor for deserialization.
	 */
	public ListingRequest() {}

	/**
	 * Constructs and fills all attributes.
	 * 
	 * @param listFilter
	 *            {@link ListFilter} filtering all {@link UserItem}s
	 * @param firstKey
	 *            key of newest userItem client possesses
	 * @param lastKey
	 *            key of oldest userItem client possesses
	 * @param fetchCount
	 *            number of items client is asking for
	 */
	public ListingRequest(ListFilter listFilter, Key firstKey, Key lastKey,
			int fetchCount) {
		this.setListFilter(listFilter);
		this.setFirstKey(firstKey);
		this.setLastKey(lastKey);
		this.setFetchCount(fetchCount);
	}

	/**
	 * @return key of newest userItem client possesses, null if this is first
	 *         request
	 */
	public Key getFirstKey() {
		return firstKey;
	}

	/**
	 * @return key of oldest userItem client possesses, null if this is first
	 *         request
	 */
	public Key getLastKey() {
		return lastKey;
	}

	/**
	 * @return {@link ListFilter} filtering all {@link UserItem}s
	 */
	public ListFilter getListFilter() {
		return listFilter;
	}

	/**
	 * @param listFilter
	 *            {@link ListFilter} filtering all {@link UserItem}s
	 */
	public void setListFilter(ListFilter listFilter) {
		this.listFilter = listFilter;
	}

	/**
	 * @param firstKey
	 *            key of newest userItem client possesses, null if this is first
	 *            request
	 */
	public void setFirstKey(Key firstKey) {
		this.firstKey = firstKey;
	}

	/**
	 * @param lastKey
	 *            key of oldest userItem client possesses, null if this is first
	 *            request
	 */
	public void setLastKey(Key lastKey) {
		this.lastKey = lastKey;
	}

	/**
	 * @return number of items client is asking for
	 */
	public int getFetchCount() {
		return fetchCount;
	}

	/**
	 * @param fetchCount
	 *            number of items client is asking for
	 */
	public void setFetchCount(int fetchCount) {
		this.fetchCount = fetchCount;
	}
}
