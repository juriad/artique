package cz.artique.shared.list;

import java.util.Date;
import java.util.List;

public class ListingUpdate<E> {
	private final List<E> head;
	private final List<E> modified;
	private final List<E> tail;
	private final Date fetched;
	private final boolean endReached;

	public ListingUpdate(List<E> head, List<E> modified, List<E> tail,
			Date fetched, boolean endReached) {
		this.head = head;
		this.modified = modified;
		this.tail = tail;
		this.fetched = fetched;
		this.endReached = endReached;
	}

	public List<E> getHead() {
		return head;
	}

	public List<E> getModified() {
		return modified;
	}

	public List<E> getTail() {
		return tail;
	}

	public Date getFetched() {
		return fetched;
	}

	public boolean isEndReached() {
		return endReached;
	}
}
