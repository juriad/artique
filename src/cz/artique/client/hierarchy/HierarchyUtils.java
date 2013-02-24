package cz.artique.client.hierarchy;

import java.util.List;

import cz.artique.shared.utils.HasHierarchy;
import cz.artique.shared.utils.HasName;

public class HierarchyUtils {
	public static final String splitSign = "/";

	private HierarchyUtils() {}

	public static <E extends HasName & HasHierarchy> Hierarchy<E> buildHierarchy(
			List<E> list) {
		Hierarchy<E> root = createRootNode();
		for (E e : list) {
			add(root, e);
		}
		return root;
	}

	public static <E extends HasName & HasHierarchy> InnerNode<E> createRootNode() {
		return new InnerNode<E>(splitSign, null);
	}

	public static <E extends HasName & HasHierarchy> boolean add(
			Hierarchy<E> root, E e) {
		if (!(root instanceof InnerNode)) {
			return false;
		}
		String[] parts = e.getHierarchy().split(splitSign);
		InnerNode<E> parent = (InnerNode<E>) root;
		for (String part : parts) {
			if (part.isEmpty()) {
				continue; // this should happen only for the first part,
				// which is root
			}
			InnerNode<E> foundChild = null;
			for (Hierarchy<E> child : parent.getChildren()) {
				if (child.getName().equals(part)) {
					if (child instanceof InnerNode) {
						foundChild = (InnerNode<E>) child;
					} else {
						return false;
					}
				}
			}
			if (foundChild != null) {
				parent = foundChild;
			} else {
				foundChild = new InnerNode<E>(part, parent);
				parent.addChild(foundChild);
				parent = foundChild;
			}
		}

		LeafNode<E> leaf = new LeafNode<E>(e, parent);
		parent.addChild(leaf);

		return true;
	}

	public static <E extends HasName & HasHierarchy> Hierarchy<E> findInTree(
			Hierarchy<E> root, E e) {
		if (!(root instanceof InnerNode)) {
			return null;
		}
		String[] parts = e.getHierarchy().split(splitSign);
		InnerNode<E> parent = (InnerNode<E>) root;
		for (String part : parts) {
			if (part.isEmpty()) {
				continue; // this should happen only for the first part,
				// which is root
			}
			InnerNode<E> foundChild = null;
			for (Hierarchy<E> child : parent.getChildren()) {
				if (child.getName().equals(part)) {
					if (child instanceof InnerNode) {
						foundChild = (InnerNode<E>) child;
					} else {
						return null;
					}
				}
			}
			if (foundChild != null) {
				parent = foundChild;
			} else {
				return null;
			}
		}

		// parent is right here
		for (Hierarchy<E> child : parent.getChildren()) {
			if (child instanceof LeafNode) {
				if (((LeafNode<E>) child).getItem().equals(e)) {
					return child;
				}
			}
		}
		return null;
	}

	public static <E extends HasName & HasHierarchy> boolean remove(
			Hierarchy<E> root, E e) {
		Hierarchy<E> inTree = findInTree(root, e);
		if (inTree == null) {
			return false;
		}

		do {
			((InnerNode<E>) inTree.getParent()).removeChild(inTree);
			inTree = inTree.getParent();
		} while (inTree.getParent() != null
			&& inTree.getParent().getChildren().size() <= 1);
		return true;
	}

}
