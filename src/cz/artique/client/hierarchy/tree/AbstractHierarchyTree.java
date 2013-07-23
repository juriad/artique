package cz.artique.client.hierarchy.tree;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
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
import cz.artique.client.manager.ManagerReady;
import cz.artique.shared.utils.HasHierarchy;

public abstract class AbstractHierarchyTree<E extends HasHierarchy, F extends ProvidesHierarchy<E> & Manager>
		extends Composite {
	interface MyResources extends ClientBundle {
		@NotStrict
		@Source("Hierarchy.css")
		CssResource style();
	}

	private static final MyResources res = GWT.create(MyResources.class);

	private final ScrollPanel scrollPanel;
	private final Tree tree;
	private final HierarchyTreeWidgetFactory<E> factory;
	private TreeItem rootItem;
	private Hierarchy<E> root;
	private F manager;

	public AbstractHierarchyTree(final F manager,
			final HierarchyTreeWidgetFactory<E> factory) {
		res.style().ensureInjected();
		this.manager = manager;
		this.factory = factory;
		scrollPanel = new ScrollPanel();
		initWidget(scrollPanel);

		tree = new Tree();
		tree.setStylePrimaryName("hierarchyTree");
		scrollPanel.add(getTree());

		manager.ready(new ManagerReady() {
			public void onReady() {
				root = manager.getHierarchyRoot();
				rootItem = createTree(getRoot());
				getTree().addItem(rootItem);

				getRoot().addHierarchyChangeHandler(
					new HierarchyChangeHandler<E>() {

						public void onHierarchyChange(
								HierarchyChangeEvent<E> event) {
							if (HierarchyChangeType.ADDED.equals(event
								.getChangeType())) {
								TreeItem inTree =
									findInTree(event.getChanged().getParent(),
										rootItem);
								if (inTree != null) {
									int index = event.getChanged().getIndex();
									HierarchyTreeWidget<E> w =
										factory.createWidget(event.getChanged());
									inTree.insertItem(index,
										new TreeItem(w.asWidget()));
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
								int index = event.getChanged().getIndex();
								TreeItem parent = inTree.getParentItem();
								parent.insertItem(index, inTree);
								getHierarchyWidget(inTree).refresh();
							}
							afterUpdate(event);
						}
					});
				initialized();
			}
		});
	}

	protected void afterUpdate(HierarchyChangeEvent<E> event) {}

	protected void initialized() {}

	@SuppressWarnings("unchecked")
	protected HierarchyTreeWidget<E> getHierarchyWidget(TreeItem item) {
		return ((HierarchyTreeWidget<E>) item.getWidget());
	}

	protected TreeItem findInTree(Hierarchy<E> hierarchy, TreeItem rootItem) {
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

	protected Tree getTree() {
		return tree;
	}

	protected Hierarchy<E> getRoot() {
		return root;
	}

	protected void expand(int levels) {
		expand(getRootItem(), levels);
	}

	private void expand(TreeItem item, int levels) {
		item.setState(true);
		for (int i = 0; i < item.getChildCount(); i++) {
			expand(item.getChild(i), levels - 1);
		}
	}

	Boolean hasAdhocTreeItem = null;
	TreeItem adhocTreeItem = null;

	protected TreeItem getAdhocTreeItem() {
		if (hasAdhocTreeItem == null) {
			Hierarchy<E> adhocItem = manager.getAdhocItem();
			if (adhocItem == null) {
				hasAdhocTreeItem = false;
			} else {
				adhocTreeItem = findInTree(adhocItem, getRootItem());
				hasAdhocTreeItem = true;
			}
		}
		return adhocTreeItem;
	}

	List<HierarchyTreeWidget<E>> selectedItems =
		new ArrayList<HierarchyTreeWidget<E>>();

	protected void select(List<HierarchyTreeWidget<E>> widgets) {
		for (HierarchyTreeWidget<E> lf : selectedItems) {
			lf.setSelected(false);
		}

		if (widgets != null && widgets.size() > 0) {
			for (HierarchyTreeWidget<E> lf : widgets) {
				lf.setSelected(true);
				selectedItems.add(lf);
			}
		}
	}
}
