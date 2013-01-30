package cz.artique.shared.items;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ListingUpdate<E> implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<E> head;
	private List<E> tail;
	private Date fetched;
	private boolean endReached;

	public ListingUpdate() {}

	public ListingUpdate(List<E> head, List<E> tail, Date fetched,
			boolean endReached) {
		this.setHead(head);
		this.setTail(tail);
		this.setFetched(fetched);
		this.setEndReached(endReached);
	}

	public List<E> getHead() {
		return head;
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

	public void setHead(List<E> head) {
		this.head = head;
	}

	public void setTail(List<E> tail) {
		this.tail = tail;
	}

	public void setFetched(Date fetched) {
		this.fetched = fetched;
	}

	public void setEndReached(boolean endReached) {
		this.endReached = endReached;
	}
}
