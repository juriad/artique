package cz.artique.client.hierarchy;

import cz.artique.shared.utils.HasHierarchy;

/**
 * LeafNode sorted by time when they were created rather than their value.
 * 
 * @author Adam Juraszek
 * 
 * @param <E>
 */
public class TimedLeafNode<E extends HasHierarchy> extends LeafNode<E> {
	private long time;

	public TimedLeafNode(E item, Hierarchy<E> parent) {
		super(item, parent);
		setTime(System.currentTimeMillis());
	}

	/**
	 * @return time when node has been created
	 */
	public long getTime() {
		return time;
	}

	/**
	 * @param time
	 *            time when node has been created
	 */
	public void setTime(long time) {
		this.time = time;
		fireChanged();
	}

	@Override
	public int compareTo(Hierarchy<E> o) {
		return -((Long) time).compareTo(((TimedLeafNode<E>) o).getTime());
	}

}
