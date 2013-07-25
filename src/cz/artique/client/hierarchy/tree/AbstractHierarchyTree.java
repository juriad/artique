package cz.artique.client.hierarchy.tree;

import java.util.ArrayList;
import java.util.List;

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

/**
 * Wraps standard {@link Tree} into {@link ScrollPanel} and adjusts it to
 * support hierarchical operations.
 * 
 * @author Adam Juraszek
 * 
 * @param <E>
 *            type of hierarchy
 * @param <F>
 *            manager providing hierarchy
 */
public abstract class AbstractHierarchyTree<E extends HasHierarchy, F extends ProvidesHierarchy<E> & Manager>
		extends Composite {

	private final ScrollPanel scrollPanel;
	private final Tree tree;
	private final HierarchyTreeWidgetFactory<E> factory;
	private TreeItem rootItem;
	private Hierarchy<E> root;
	private F manager;

	/**
	 * As soon as manager is ready, builds {@link Hierarchy} and wraps it into
	 * {@link TreeItem}s and adds it into {@link Tree}.
	 * 
	 * @param manager
	 *            manager providing hierarchy
	 * @param factory
	 *            factory to create {@link HierarchyTreeWidget}s
	 */
	public AbstractHierarchyTree(final F manager,
			final HierarchyTreeWidgetFactory<E> factory) {
		HierarchyResources.INSTANCE.style().ensureInjected();
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
										createHierarchyWidget(event
											.getChanged());
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
							afterHierarchyChange(event);
						}
					});
				initialized();
			}
		});
	}

	/**
	 * Allows extended functionality to be hooked to the end of
	 * {@link HierarchyChangeEvent} processing.
	 * 
	 * @param event
	 *            processed event
	 */
	protected void afterHierarchyChange(HierarchyChangeEvent<E> event) {}

	/**
	 * Allows extended functionality to be hooked to the end of initialization.
	 */
	protected void initialized() {}

	/**
	 * Gets {@link HierarchyTreeWidget} from {@link TreeItem}.
	 * 
	 * @param item
	 *            treeItem containing {@link HierarchyTreeWidget}
	 * @return contained {@link HierarchyTreeWidget}
	 */
	@SuppressWarnings("unchecked")
	protected HierarchyTreeWidget<E> getHierarchyWidget(TreeItem item) {
		return ((HierarchyTreeWidget<E>) item.getWidget());
	}

	/**
	 * Hierarchically search tree for hierarchy object.
	 * 
	 * @param hierarchy
	 *            hierarchy object
	 * @param rootItem
	 *            tree item
	 * @return tree item containing hierarchy object or null if not found
	 */
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

	/**
	 * @param hierarchy
	 *            hierarchy object
	 * @return created {@link HierarchyTreeWidget}
	 */
	protected HierarchyTreeWidget<E> createHierarchyWidget(
			Hierarchy<E> hierarchy) {
		HierarchyTreeWidget<E> w = factory.createWidget(hierarchy);
		return w;
	}

	/**
	 * Rebuilds general hierarchy into hierarchy of {@link TreeItem}s.
	 * 
	 * @param root
	 *            hierarchy object
	 * @return corresponding {@link TreeItem}
	 */
	private TreeItem createTree(Hierarchy<E> root) {
		HierarchyTreeWidget<E> w = createHierarchyWidget(root);
		TreeItem rootItem = new TreeItem(w.asWidget());
		for (Hierarchy<E> child : root.getChildren()) {
			TreeItem childItem = createTree(child);
			rootItem.addItem(childItem);
		}
		return rootItem;
	}

	/**
	 * @return root {@link TreeItem}
	 */
	protected TreeItem getRootItem() {
		return rootItem;
	}

	/**
	 * @return {@link Tree}
	 */
	protected Tree getTree() {
		return tree;
	}

	/**
	 * @return root node of hierarchy
	 */
	protected Hierarchy<E> getRoot() {
		return root;
	}

	/**
	 * Expands {@link Tree}.
	 * 
	 * @param levels
	 *            number of levels to expand
	 */
	protected void expand(int levels) {
		expand(getRootItem(), levels);
	}

	/**
	 * Recursively expand {@link Tree}.
	 * 
	 * @param item
	 *            {@link TreeItem}
	 * @param levels
	 *            number of levels to expand
	 */
	private void expand(TreeItem item, int levels) {
		item.setState(true);
		for (int i = 0; i < item.getChildCount(); i++) {
			expand(item.getChild(i), levels - 1);
		}
	}

	Boolean hasAdhocTreeItem = null;
	TreeItem adhocTreeItem = null;

	/**
	 * @return {@link TreeItem} corresponding to adhoc hierarchy object or null
	 *         if it does not exist
	 */
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

	/**
	 * Selects list of widgets, others will be deselected.
	 * 
	 * @param widgets
	 *            list of widgets to select
	 */
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
