package cz.artique.client.hierarchy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.shared.HandlerRegistration;

import cz.artique.shared.utils.HasHierarchy;
import cz.artique.shared.utils.HasName;
import cz.artique.shared.utils.SortedList;

public class InnerNode<E extends HasName & HasHierarchy>
		implements Hierarchy<E> {

	private final String name;
	private final Hierarchy<E> parent;
	private final SortedList<Hierarchy<E>> children;

	public InnerNode(String name, Hierarchy<E> parent) {
		this.name = name;
		this.parent = parent;
		this.children = new SortedList<Hierarchy<E>>();
	}

	public String getName() {
		return name;
	}

	public E getItem() {
		return null;
	}

	public List<Hierarchy<E>> getChildren() {
		return children;
	}

	public Hierarchy<E> getParent() {
		return parent;
	}

	private final Map<Hierarchy<E>, HandlerRegistration> registrations =
		new HashMap<Hierarchy<E>, HandlerRegistration>();

	public void addChild(Hierarchy<E> child) {
		children.add(child);
		HandlerRegistration registration =
			child.addHierarchyChangeHandler(new HierarchyChangeHandler<E>() {

				public void onHierarchyChange(HierarchyChangeEvent<E> event) {
					for (HierarchyChangeHandler<E> handler : handlers) {
						handler.onHierarchyChange(event);
					}
				}
			});
		registrations.put(child, registration);

		HierarchyChangeEvent<E> event =
			new HierarchyChangeEvent<E>(child, HierarchyChangeType.ADDED);
		for (HierarchyChangeHandler<E> handler : handlers) {
			handler.onHierarchyChange(event);
		}
	}

	public void removeChild(Hierarchy<E> child) {
		children.remove(child);
		registrations.remove(child);

		HierarchyChangeEvent<E> event =
			new HierarchyChangeEvent<E>(child, HierarchyChangeType.REMOVED);
		for (HierarchyChangeHandler<E> handler : handlers) {
			handler.onHierarchyChange(event);
		}
	}

	private final List<HierarchyChangeHandler<E>> handlers =
		new ArrayList<HierarchyChangeHandler<E>>();

	public HandlerRegistration addHierarchyChangeHandler(
			final HierarchyChangeHandler<E> handler) {
		handlers.add(handler);
		return new HandlerRegistration() {

			public void removeHandler() {
				handlers.remove(handler);
			}
		};
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		InnerNode<E> other = (InnerNode<E>) obj;
		if (!name.equals(other.name))
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		return true;
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
		for (Hierarchy<E> child : getChildren()) {
			child.getAll(list);
		}
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
		return getParent() != null ? getParent().getHierarchy() + getName()
			+ "/" : "/";
	}

}
