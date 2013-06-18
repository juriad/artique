package cz.artique.shared.items;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.ListFilterOrder;

/**
 * Represents response to {@link ListingRequest}. Contains list of newly added
 * {@link UserItem}s in case when {@link ListFilterOrder} was DESCENDING in
 * {@link #getHead()} and list of more {@link UserItem}s at the end in
 * {@link #getTail()}. The flag attribute EndReached tells weather the listing
 * contains more {@link UserItem}s.
 * 
 * @author Adam Juraszek
 * 
 */
public class ListingResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<UserItem> head;
	private List<UserItem> tail;
	private Date fetched;
	private boolean endReached;

	/**
	 * Default constructor for deserialization.
	 */
	public ListingResponse() {}

	/**
	 * Constructor filling all attributes.
	 * 
	 * @param head
	 *            new UserItems at the top of listing
	 * @param tail
	 *            more UserItems fetched when scrolled to bottom
	 * @param fetched
	 *            time-stamp of last fetch
	 * @param endReached
	 *            true if the are no more items, false otherwise
	 */
	public ListingResponse(List<UserItem> head, List<UserItem> tail,
			Date fetched, boolean endReached) {
		this.setHead(head);
		this.setTail(tail);
		this.setFetched(fetched);
		this.setEndReached(endReached);
	}

	/**
	 * @return new UserItems at the top of listing
	 */
	public List<UserItem> getHead() {
		return head;
	}

	/**
	 * @return more UserItems fetched when scrolled to bottom
	 */
	public List<UserItem> getTail() {
		return tail;
	}

	/**
	 * @return time-stamp of last fetch
	 */
	public Date getFetched() {
		return fetched;
	}

	/**
	 * @return true if the are no more items, false otherwise
	 */
	public boolean isEndReached() {
		return endReached;
	}

	/**
	 * @param head
	 *            new UserItems at the top of listing
	 */
	public void setHead(List<UserItem> head) {
		this.head = head;
	}

	/**
	 * @param tail
	 *            more UserItems fetched when scrolled to bottom
	 */
	public void setTail(List<UserItem> tail) {
		this.tail = tail;
	}

	/**
	 * @param fetched
	 *            time-stamp of last fetch
	 */
	public void setFetched(Date fetched) {
		this.fetched = fetched;
	}

	/**
	 * @param endReached
	 *            true if the are no more items, false otherwise
	 */
	public void setEndReached(boolean endReached) {
		this.endReached = endReached;
	}
}
