package cz.artique.client.hierarchy;

import cz.artique.shared.utils.HasHierarchy;
import cz.artique.shared.utils.HasName;

public class TimedLeafNode<E extends HasName & HasHierarchy>
		extends LeafNode<E> {
	private long time;

	public TimedLeafNode(E item, Hierarchy<E> parent) {
		super(item, parent);
		setTime(System.currentTimeMillis());
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
		fireChanged();
	}

	@Override
	public int compareTo(Hierarchy<E> o) {
		return -((Long) time).compareTo(((TimedLeafNode<E>) o).getTime());
	}

}
