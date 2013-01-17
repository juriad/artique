package cz.artique.client.hierarchy;

import java.util.ArrayList;
import java.util.List;

import cz.artique.shared.utils.HasHierarchy;
import cz.artique.shared.utils.HasName;

public class InnerNode<E extends HasName & HasHierarchy>
		implements Hierarchy<E> {

	private final String name;
	private final Hierarchy<E> parent;
	private final List<Hierarchy<E>> children;

	public InnerNode(String name, Hierarchy<E> parent) {
		this.name = name;
		this.parent = parent;
		this.children = new ArrayList<Hierarchy<E>>();
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

	public void addChild(Hierarchy<E> child) {
		children.add(child);
	}

}
