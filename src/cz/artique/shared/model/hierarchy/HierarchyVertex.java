package cz.artique.shared.model.hierarchy;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import cz.artique.utils.SourceException;

public class HierarchyVertex<E extends SupportsHierarchy>
		implements Serializable, Hierarchy<E> {

	private static final long serialVersionUID = 1L;

	private List<Hierarchy<E>> children;
	private List<Hierarchy<E>> removed;

	private Hierarchy<E> parent;
	private String name;
	private int order;

	public HierarchyVertex(Hierarchy<E> parent, String name, int order) {
		this.parent = parent;
		this.name = name;
		this.order = order;
	}

	public void addChild(Hierarchy<E> child) {
		children.add(child);
		child.setParent(this);
		child.setChange(HierarchyChangeType.ADDED);
	}

	public String getHierarchy() {
		if (parent == null) {
			return "/";
		}
		return parent.getName() + "[" + HierarchyUtils.formatNumber(getOrder())
			+ "]" + getName();
	}

	public void getChanges(Map<E, HierarchyChangeType> changes) {
		for (Hierarchy<E> child : getChildren()) {
			child.getChanges(changes);
		}
		for (Hierarchy<E> child : removed) {
			child.getChanges(changes);
		}
	}

	public List<Hierarchy<E>> getChildren() {
		return children;
	}

	public String getName() {
		return name;
	}

	public int getOrder() {
		return order;
	}

	public Hierarchy<E> getParent() {
		return parent;
	}

	public void removeChild(Hierarchy<E> child) {
		children.remove(child);
		child.setChange(HierarchyChangeType.REMOVED);
		removed.add(child);
	}

	public void setChange(HierarchyChangeType change) {
		if (change == HierarchyChangeType.CHANGED_ORDER) {
			order = parent.getChildren().indexOf(this);
			change = HierarchyChangeType.CHANGED;
		}

		if (change == HierarchyChangeType.ADDED) {
			order = parent.getChildren().indexOf(this);
		}

		for (Hierarchy<E> child : getChildren()) {
			child.setChange(change);
		}
	}

	public void setName(String name) {
		if (parent == null) {
			throw new SourceException("Cannot change order of root");
		}

		if (name == getName()) {
			// no change
			return;
		}

		this.name = name;
		for (Hierarchy<E> child : getChildren()) {
			child.setChange(HierarchyChangeType.CHANGED);
		}
	}

	public void setOrder(int index) {
		if (parent == null) {
			throw new SourceException("Cannot change order of root");
		}

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
