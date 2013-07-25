package cz.artique.client.hierarchy;

import java.util.List;

import cz.artique.shared.utils.HasHierarchy;

/**
 * Manipulates with hierarchy: adds, removes nodes, searches for values in tree
 * and builds tree.
 * 
 * @author Adam Juraszek
 * 
 */
public class HierarchyUtils {
	public static final String splitSign = "/";

	private HierarchyUtils() {}

	/**
	 * Builds tree form list of hierarchy objects.
	 * Creates empty tree and then calls {@link #add(Hierarchy, HasHierarchy)}
	 * for each object.
	 * 
	 * @param list
	 *            list of hierarchy objects
	 * @return hierarchical representation
	 */
	public static <E extends HasHierarchy> Hierarchy<E> buildHierarchy(
			List<E> list) {
		Hierarchy<E> root = createRootNode();
		for (E e : list) {
			add(root, e);
		}
		return root;
	}

	/**
	 * @return root node
	 */
	public static <E extends HasHierarchy> InnerNode<E> createRootNode() {
		return new InnerNode<E>(splitSign, null);
	}

	/**
	 * Add a new object value to the tree.
	 * Splits path to root by separator (slash) and for each part creates (if it
	 * does not exist) an {@link InnerNode}.
	 * The object itself wraps into {@link LeafNode} and adds as child to
	 * bottom-most {@link InnerNode}.
	 * 
	 * @param root
	 *            tree represented by its root node
	 * @param e
	 *            hierarchy object
	 * @return whether tree has been changed
	 */
	public static <E extends HasHierarchy> boolean add(Hierarchy<E> root, E e) {
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

	/**
	 * Searches the tree for {@link Hierarchy} which has value equal to
	 * hierarchy object.
	 * 
	 * The process is similar to the one performed by
	 * {@link #add(Hierarchy, HasHierarchy)}.
	 * 
	 * @param root
	 *            tree represented by its root node
	 * @param e
	 *            hierarchy object
	 * @return {@link Hierarchy} with desired value
	 */
	public static <E extends HasHierarchy> Hierarchy<E> findInTree(
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

	/**
	 * Removes hierarchy object from hierarchy
	 * 
	 * @param root
	 *            tree represented by its root node
	 * @param e
	 *            hierarchy object
	 * @return whether tree has been changed
	 */
	public static <E extends HasHierarchy> boolean remove(Hierarchy<E> root, E e) {
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
