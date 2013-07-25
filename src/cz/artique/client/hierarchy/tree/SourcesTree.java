package cz.artique.client.hierarchy.tree;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.user.client.ui.TreeItem;

import cz.artique.client.hierarchy.Hierarchy;
import cz.artique.client.hierarchy.HierarchyChangeEvent;
import cz.artique.client.hierarchy.HierarchyTreeWidget;
import cz.artique.client.hierarchy.HierarchyUtils;
import cz.artique.client.hierarchy.tree.UserSourceWidget.UserSourceWidgetFactory;
import cz.artique.client.history.HistoryEvent;
import cz.artique.client.history.HistoryHandler;
import cz.artique.client.history.HistoryItem;
import cz.artique.client.history.HistoryManager;
import cz.artique.client.manager.Managers;
import cz.artique.client.sources.SourcesManager;
import cz.artique.shared.model.label.Filter;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.LabelType;
import cz.artique.shared.model.label.ListFilter;
import cz.artique.shared.model.source.UserSource;

/**
 * Tree containing hierarchy of {@link UserSource}s.
 * 
 * @author Adam Juraszek
 * 
 */
public class SourcesTree
		extends AbstractHierarchyTree<UserSource, SourcesManager> {

	public SourcesTree() {
		super(Managers.SOURCES_MANAGER, UserSourceWidgetFactory.FACTORY);
	}

	/**
	 * Observe history change.
	 * When history is changed, select those sources which are used in current
	 * {@link ListFiltr}.
	 */
	private void observeHistoryChange() {
		HistoryManager.HISTORY.addHistoryHandler(new HistoryHandler() {
			@SuppressWarnings("unchecked")
			public void onHistoryChanged(HistoryEvent e) {
				TreeItem rootItem = getRootItem();
				if (rootItem == null) {
					return;
				}
				refreshAll(rootItem);

				List<HierarchyTreeWidget<UserSource>> allSourcesWidgets =
					getAllSourcesWidgets();
				if (allSourcesWidgets.isEmpty()) {
					allSourcesWidgets
						.add((HierarchyTreeWidget<UserSource>) getRootItem()
							.getWidget());
				}
				select(allSourcesWidgets);
			}
		});
	}

	/**
	 * Refresh all
	 * 
	 * @see cz.artique.client.hierarchy.tree.AbstractHierarchyTree#afterHierarchyChange(cz.artique.client.hierarchy.HierarchyChangeEvent)
	 */
	@Override
	protected void afterHierarchyChange(HierarchyChangeEvent<UserSource> event) {
		refreshAll(getRootItem());
	}

	/**
	 * Gets widgets corresponding to labels of sources used in current
	 * {@link ListFilter}.
	 * 
	 * @return list of widgets
	 */
	@SuppressWarnings("unchecked")
	protected List<HierarchyTreeWidget<UserSource>> getAllSourcesWidgets() {
		List<HierarchyTreeWidget<UserSource>> allSourcesWidgets =
			new ArrayList<HierarchyTreeWidget<UserSource>>();

		List<Label> allSourcesLabels = getAllSourcesLabels();
		for (Label l : allSourcesLabels) {
			UserSource byLabel = Managers.SOURCES_MANAGER.getByLabel(l);
			if (byLabel != null) {
				Hierarchy<UserSource> findInTree =
					HierarchyUtils.findInTree(getRoot(), byLabel);
				if (findInTree != null) {
					TreeItem inTree = findInTree(findInTree, getRootItem());
					if (inTree != null) {
						allSourcesWidgets
							.add((HierarchyTreeWidget<UserSource>) inTree
								.getWidget());
					}
				}
			}
		}
		return allSourcesWidgets;
	}

	/**
	 * Gets labels of all sources used in current {@link ListFilter}.
	 * 
	 * @return list of labels
	 */
	protected List<Label> getAllSourcesLabels() {
		List<Label> labels = new ArrayList<Label>();
		HistoryItem historyItem = HistoryManager.HISTORY.getLastHistoryItem();
		if (historyItem != null) {
			ListFilter listFilter = historyItem.getListFilter();
			Filter filterObject = listFilter.getFilterObject();
			if (filterObject != null) {
				List<Key> keys = filterObject.flat();
				List<Label> sortedList =
					Managers.LABELS_MANAGER.getSortedList(keys);
				for (Label l : sortedList) {
					if (LabelType.USER_SOURCE.equals(l.getLabelType())) {
						labels.add(l);
					}
				}
			}
		}
		return labels;
	}

	/**
	 * Change visibility of according to show-hide status.
	 * 
	 * @param rootItem
	 *            {@link TreeItem}
	 * @return whether the {@link TreeItem} shall be visible
	 */
	protected boolean refreshAll(TreeItem rootItem) {
		boolean visible = rootItem.equals(getRootItem());
		for (int i = 0; i < rootItem.getChildCount(); i++) {
			boolean v = refreshAll(rootItem.getChild(i));
			if (v) {
				visible = true;
			}
		}
		HierarchyTreeWidget<UserSource> w = getHierarchyWidget(rootItem);
		boolean v2 = w.refresh();
		if (v2) {
			visible = true;
		}
		rootItem.setVisible(visible || showDisabled);
		return visible;
	}

	/**
	 * Observe history, expand two levels and refresh all.
	 * 
	 * @see cz.artique.client.hierarchy.tree.AbstractHierarchyTree#initialized()
	 */
	@Override
	protected void initialized() {
		observeHistoryChange();
		expand(2);
		refreshAll(getRootItem());
	}

	/**
	 * @return shall disabled {@link UserSource}s be shown
	 */
	public boolean isShowingDisabled() {
		return showDisabled;
	}

	boolean showDisabled = false;

	/**
	 * Toggles whether disabled {@link UserSource}s shall be shown.
	 */
	public void toggleDisabled() {
		showDisabled = !showDisabled;
		refreshAll(getRootItem());
	}

	/**
	 * Injects reference to {@link SourcesTree} into the
	 * {@link UserSourceWidget}.
	 * 
	 * @see cz.artique.client.hierarchy.tree.AbstractHierarchyTree#createHierarchyWidget(cz.artique.client.hierarchy.Hierarchy)
	 */
	@Override
	protected HierarchyTreeWidget<UserSource> createHierarchyWidget(
			Hierarchy<UserSource> hierarchy) {
		HierarchyTreeWidget<UserSource> hierarchyWidget =
			super.createHierarchyWidget(hierarchy);
		if (hierarchyWidget instanceof UserSourceWidget) {
			((UserSourceWidget) hierarchyWidget).setSourcesTree(this);
		}
		return hierarchyWidget;
	}
}
