package cz.artique.shared.model.hierarchy;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import cz.artique.utils.SourceException;

public class HierarchyLeaf<E extends SupportsHierarchy>
		implements Serializable, Hierarchy<E> {

	private static final long serialVersionUID = 1L;

	private Hierarchy<E> parent;
	private int order;
	private E e;

	private HierarchyChangeType change = HierarchyChangeType.NO_CHANGE;

	public HierarchyLeaf(E e) {
		this.e = e;
		parent = null;
		order = -1;
	}

	public HierarchyLeaf(E e, Hierarchy<E> parent, int order) {
		this.e = e;
		this.parent = parent;
		this.order = order;
	}

	public void addChild(Hierarchy<E> child) {
		throw new SourceException("Cannot remove child of a leaf");
	}

	public E getE() {
		return e;
	}

	public String getHierarchy() {
		return parent.getName() + "[" + HierarchyUtils.formatNumber(getOrder())
			+ "]";
	}

	public void getChanges(Map<E, HierarchyChangeType> changes) {
		if (change != HierarchyChangeType.NO_CHANGE) {
			e.setHierarchy(getHierarchy());
			changes.put(e, change);
		}
	}

	public List<Hierarchy<E>> getChildren() {
		return Collections.emptyList();
	}

	public String getName() {
		return e.getName();
	}

	public int getOrder() {
		return order;
	}

	public Hierarchy<E> getParent() {
		return parent;
	}

	public void removeChild(Hierarchy<E> child) {
		throw new SourceException("Cannot remove child of a leaf");
	}

	public void setChange(HierarchyChangeType change) {
		if (change == HierarchyChangeType.CHANGED_ORDER) {
			order = parent.getChildren().indexOf(this);
			change = HierarchyChangeType.CHANGED;
		}

		if (change == HierarchyChangeType.ADDED) {
			order = parent.getChildren().indexOf(this);
		}

		this.change = change;
	}

	public void setName(String name) {
		e.setName(name);
		change = HierarchyChangeType.CHANGED;
	}

	public void setOrder(int index) {
		List<Hierarchy<E>> siblings = parent.getChildren();
		if (order < 0) {
			order = 0;
		} else if (order >= siblings.size()) {
			order = siblings.size() - 1;
		}

		if (order == getOrder()) {
			// no change
			return;
		}

		siblings.add(order, siblings.remove(getOrder()));

		for (int i = 0; i < siblings.size(); i++) {
			Hierarchy<E> sibling = siblings.get(i);
			if (sibling.getOrder() != i) {
				sibling.setChange(HierarchyChangeType.CHANGED_ORDER);
			}
		}
	}

	public void setParent(Hierarchy<E> parent) {
		this.parent = parent;
		order = parent.getChildren().indexOf(this);
	}

}
