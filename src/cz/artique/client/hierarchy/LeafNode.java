package cz.artique.client.hierarchy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;

import cz.artique.shared.utils.HasHierarchy;
import cz.artique.shared.utils.HasName;

public class LeafNode<E extends HasName & HasHierarchy> implements Hierarchy<E> {

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

	public HandlerRegistration addHierarchyChangeHandler(
			final HierarchyChangeHandler<E> handler) {
		handlers.add(handler);
		return new HandlerRegistration() {

			public void removeHandler() {
				handlers.remove(handler);
			}
		};
	}

}
