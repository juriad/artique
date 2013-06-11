package cz.artique.client.hierarchy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;

import cz.artique.shared.utils.HasHierarchy;

public class LeafNode<E extends HasHierarchy> implements Hierarchy<E> {

	private final Hierarchy<E> parent;
	private final E item;

	public LeafNode(E item, Hierarchy<E> parent) {
		this.item = item;
		this.parent = parent;
	}

	public String getName() {
		return item.getName();
	}

	public E getItem() {
		return item;
	}

	public List<Hierarchy<E>> getChildren() {
		return Collections.emptyList();
	}

	public Hierarchy<E> getParent() {
		return parent;
	}

	private final List<HierarchyChangeHandler<E>> handlers =
		new ArrayList<HierarchyChangeHandler<E>>();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((item == null) ? 0 : item.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		LeafNode<E> other = (LeafNode<E>) obj;
		if (!item.equals(other.item))
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		return true;
	}

	public HandlerRegistration addHierarchyChangeHandler(
			final HierarchyChangeHandler<E> handler) {
		handlers.add(handler);
		return new HandlerRegistration() {

			public void removeHandler() {
				handlers.remove(handler);
			}
		};
	}

	public int compareTo(Hierarchy<E> o) {
		int result = getName().compareToIgnoreCase(o.getName());
		return result;
	}

	public void fireChanged() {
		for (HierarchyChangeHandler<E> handler : handlers) {
			handler.onHierarchyChange(new HierarchyChangeEvent<E>(this,
				HierarchyChangeType.CHANGED));
		}
	}

	public void getAll(List<E> list) {
		list.add(item);
	}

	public List<Hierarchy<E>> getSiblings() {
		if (getParent() != null) {
			return getParent().getChildren();
		}
		ArrayList<Hierarchy<E>> arrayList = new ArrayList<Hierarchy<E>>();
		arrayList.add(this);
		return arrayList;
	}

	public int getIndex() {
		return getSiblings().indexOf(this);
	}

	public String getHierarchy() {
		return getItem().getHierarchy();
	}

}
