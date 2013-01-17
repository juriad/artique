package cz.artique.client.hierarchy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.artique.shared.utils.HasHierarchy;
import cz.artique.shared.utils.HasName;

public class HierarchyUtils {
	public static final String splitSign = "/";

	private HierarchyUtils() {}

	public <E extends HasName & HasHierarchy> Hierarchy<E> buildHierarchy(
			List<E> list) {
		class H {
			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result + childName.hashCode();
				result = prime * result + parent.hashCode();
				return result;
			}

			@Override
			public boolean equals(Object obj) {
				H other = (H) obj;
				if (!childName.equals(other.childName))
					return false;
				if (parent == null) {
					if (other.parent != null)
						return false;
				} else if (!parent.equals(other.parent))
					return false;
				return true;
			}

			Hierarchy<E> parent;
			String childName;

			H(String childName, Hierarchy<E> parent) {
				this.childName = childName;
				this.parent = parent;
			}
		}
		Map<H, InnerNode<E>> hs = new HashMap<H, InnerNode<E>>();
		InnerNode<E> root = new InnerNode<E>("", null);
		hs.put(new H(root.getName(), null), root);

		for (E e : list) {
			String[] parts = e.getHierarchy().split(splitSign);
			InnerNode<E> parent = root;
			for (int i = 1; i < parts.length - 1; i++) {
				String part = parts[i];
				InnerNode<E> parent2 = hs.get(new H(part, parent));
				if (parent2 == null) {
					parent2 = new InnerNode<E>(part, parent);
					hs.put(new H(part, parent), parent2);
					parent.addChild(parent2);
				}
				parent = parent2;
			}
			LeafNode<E> leaf = new LeafNode<E>(e, parent);
			parent.addChild(leaf);
		}
		return root;
	}
}
