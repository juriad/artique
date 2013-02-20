package cz.artique.client.artiqueHierarchy;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

import cz.artique.client.hierarchy.Hierarchy;
import cz.artique.client.hierarchy.HierarchyChangeEvent;
import cz.artique.client.hierarchy.HierarchyChangeHandler;
import cz.artique.client.hierarchy.HierarchyChangeType;
import cz.artique.client.hierarchy.HierarchyTreeWidget;
import cz.artique.client.hierarchy.HierarchyTreeWidgetFactory;
import cz.artique.client.hierarchy.ProvidesHierarchy;
import cz.artique.client.manager.Manager;
import cz.artique.shared.utils.HasHierarchy;
import cz.artique.shared.utils.HasName;

public class AbstractHierarchyTree<E extends HasHierarchy & HasName, F extends ProvidesHierarchy<E> & Manager>
		extends Composite {
	private final ScrollPanel scrollPanel;
	private final Tree tree;
	private final HierarchyTreeWidgetFactory<E> factory;
	private TreeItem rootItem;

	public AbstractHierarchyTree(final F manager,
			final HierarchyTreeWidgetFactory<E> factory) {
		this.factory = factory;
		scrollPanel = new ScrollPanel();
		initWidget(scrollPanel);

		tree = new Tree();
		scrollPanel.add(tree);

		manager.ready(new AsyncCallback<Void>() {

			public void onSuccess(Void result) {
				Hierarchy<E> root = manager.getHierarchyRoot();
				rootItem = createTree(root);
				tree.addItem(rootItem);

				root.addHierarchyChangeHandler(new HierarchyChangeHandler<E>() {

					public void onHierarchyChange(HierarchyChangeEvent<E> event) {
						if (HierarchyChangeType.ADDED.equals(event
							.getChangeType())) {
							TreeItem inTree =
								findInTree(event.getChanged().getParent(),
									rootItem);
							if (inTree != null) {
								HierarchyTreeWidget<E> w =
									factory.createWidget(event.getChanged());
								inTree.addItem(new TreeItem(w.asWidget()));
							}
						} else if (HierarchyChangeType.REMOVED.equals(event
							.getChangeType())) {
							TreeItem inTree =
								findInTree(event.getChanged(), rootItem);
							if (inTree != null) {
								inTree.remove();
							}
						} else if (HierarchyChangeType.CHANGED.equals(event
							.getChangeType())) {
							TreeItem inTree =
								findInTree(event.getChanged().getParent(),
									rootItem);
							getHierarchyWidget(inTree).refresh();
						}
					}
				});
			}

			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}
		});
	}

	@SuppressWarnings("unchecked")
	protected HierarchyTreeWidget<E> getHierarchyWidget(TreeItem item) {
		return ((HierarchyTreeWidget<E>) item);
	}

	private TreeItem findInTree(Hierarchy<E> hierarchy, TreeItem rootItem) {
		if (getHierarchyWidget(rootItem).getHierarchy().equals(hierarchy)) {
			return rootItem;
		}
		for (int i = 0; i < rootItem.getChildCount(); i++) {
			TreeItem item = rootItem.getChild(i);
			TreeItem findInTree = findInTree(hierarchy, item);
			if (findInTree != null) {
				return findInTree;
			}
		}
		return null;
	}

	private TreeItem createTree(Hierarchy<E> root) {
		HierarchyTreeWidget<E> w = factory.createWidget(root);
		TreeItem rootItem = new TreeItem(w.asWidget());
		for (Hierarchy<E> child : root.getChildren()) {
			TreeItem childItem = createTree(child);
			rootItem.addItem(childItem);
		}
		return rootItem;
	}

	protected TreeItem getRootItem() {
		return rootItem;
	}
}
