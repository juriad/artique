package cz.artique.client.artiqueHierarchy;


import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;

import cz.artique.client.artiqueSources.ProvidesHierarchy;
import cz.artique.client.hierarchy.Hierarchy;
import cz.artique.client.hierarchy.HierarchyChangeEvent;
import cz.artique.client.hierarchy.HierarchyChangeHandler;
import cz.artique.client.hierarchy.HierarchyChangeType;
import cz.artique.client.hierarchy.HierarchyTreeItem;
import cz.artique.client.hierarchy.HierarchyTreeItemFactory;
import cz.artique.client.manager.Manager;
import cz.artique.shared.utils.HasHierarchy;
import cz.artique.shared.utils.HasName;

public class AbstractHierarchyTree<E extends HasHierarchy & HasName, F extends ProvidesHierarchy<E> & Manager>
		extends Composite {
	private final ScrollPanel scrollPanel;
	private final Tree tree;
	private final HierarchyTreeItemFactory<E> factory;

	public AbstractHierarchyTree(final F manager,
			final HierarchyTreeItemFactory<E> factory) {
		this.factory = factory;
		scrollPanel = new ScrollPanel();
		initWidget(scrollPanel);

		tree = new Tree();
		scrollPanel.add(tree);

		manager.ready(new AsyncCallback<Void>() {

			public void onSuccess(Void result) {
				Hierarchy<E> root = manager.getHierarchyRoot();
				final HierarchyTreeItem<E> rootItem = getItemsTree(root);
				tree.addItem(rootItem);

				root.addHierarchyChangeHandler(new HierarchyChangeHandler<E>() {

					public void onHierarchyChange(HierarchyChangeEvent<E> event) {
						if (HierarchyChangeType.ADDED.equals(event
							.getChangeType())) {
							HierarchyTreeItem<E> inTree =
								findInTree(event.getChanged().getParent(),
									rootItem);
							if (inTree != null) {
								inTree.addItem(factory.createTreeItem(event
									.getChanged()));
							}
						} else if (HierarchyChangeType.REMOVED.equals(event
							.getChangeType())) {
							HierarchyTreeItem<E> inTree =
								findInTree(event.getChanged(), rootItem);
							if (inTree != null) {
								inTree.remove();
							}
						} else if (HierarchyChangeType.CHANGED.equals(event
							.getChangeType())) {
							HierarchyTreeItem<E> inTree =
								findInTree(event.getChanged().getParent(),
									rootItem);
							inTree.refresh();
						}
					}
				});
			}

			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}
		});
	}

	protected HierarchyTreeItem<E> findInTree(Hierarchy<E> hierarchy,
			HierarchyTreeItem<E> rootItem) {
		if (rootItem.getHierarchy().equals(hierarchy)) {
			return rootItem;
		}
		for (int i = 0; i < rootItem.getChildCount(); i++) {
			@SuppressWarnings("unchecked")
			HierarchyTreeItem<E> item =
				(HierarchyTreeItem<E>) rootItem.getChild(i);
			HierarchyTreeItem<E> findInTree = findInTree(hierarchy, item);
			if (findInTree != null) {
				return findInTree;
			}
		}
		return null;
	}

	private HierarchyTreeItem<E> getItemsTree(Hierarchy<E> root) {
		HierarchyTreeItem<E> rootItem = factory.createTreeItem(root);
		System.out.println(root.getName() +":  "+ root + ":  " + root.getParent());
		for (Hierarchy<E> child : root.getChildren()) {
			HierarchyTreeItem<E> childItem = getItemsTree(child);
			rootItem.addItem(childItem);
		}
		return rootItem;
	}
}
