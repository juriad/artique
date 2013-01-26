package cz.artique.client.artiqueHierarchy;

import cz.artique.client.hierarchy.Hierarchy;
import cz.artique.client.hierarchy.HierarchyTreeItem;
import cz.artique.client.hierarchy.HierarchyTreeItemFactory;
import cz.artique.client.hierarchy.HierarchyUtils;
import cz.artique.client.hierarchy.LeafNode;
import cz.artique.shared.model.source.UserSource;

public class UserSourceTreeItemFactory
		implements HierarchyTreeItemFactory<UserSource> {
	public static final UserSourceTreeItemFactory factory =
		new UserSourceTreeItemFactory();

	public HierarchyTreeItem<UserSource> createTreeItem(
			Hierarchy<UserSource> hierarchy) {

		HierarchyTreeItem<UserSource> item =
			new HierarchyTreeItem<UserSource>(hierarchy);
		if (hierarchy instanceof LeafNode) {
			LeafNode<UserSource> leaf = (LeafNode<UserSource>) hierarchy;
			UserSourceWidget w = new UserSourceWidget(leaf.getItem());
			item.setWidget(w);
		} else if (hierarchy.getParent() == null) {
			item.setText(HierarchyUtils.splitSign);
		} else {
			item.setText(hierarchy.getName());
		}
		return item;
	}

}
