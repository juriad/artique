package cz.artique.client.artiqueHierarchy;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.user.client.ui.TreeItem;

import cz.artique.client.artiqueHierarchy.UserSourceWidget.UserSourceWidgetFactory;
import cz.artique.client.artiqueHistory.ArtiqueHistory;
import cz.artique.client.artiqueHistory.HistoryEvent;
import cz.artique.client.artiqueHistory.HistoryHandler;
import cz.artique.client.artiqueHistory.HistoryItem;
import cz.artique.client.artiqueSources.ArtiqueSourcesManager;
import cz.artique.client.hierarchy.Hierarchy;
import cz.artique.client.hierarchy.HierarchyTreeWidget;
import cz.artique.client.hierarchy.HierarchyUtils;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.label.Filter;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.LabelType;
import cz.artique.shared.model.label.ListFilter;
import cz.artique.shared.model.source.UserSource;

public class ArtiqueSourcesTree
		extends AbstractHierarchyTree<UserSource, ArtiqueSourcesManager> {

	public ArtiqueSourcesTree() {
		super(Managers.SOURCES_MANAGER, UserSourceWidgetFactory.FACTORY);
	}

	private void observeHistoryChange() {
		ArtiqueHistory.HISTORY.addHistoryHandler(new HistoryHandler() {
			public void onHistoryChanged(HistoryEvent e) {
				TreeItem rootItem = getRootItem();
				if (rootItem == null) {
					return;
				}
				refreshAll(rootItem);

				select(getAllSourcesWidgets());
			}
		});
	}

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

	protected List<Label> getAllSourcesLabels() {
		List<Label> labels = new ArrayList<Label>();
		HistoryItem historyItem = ArtiqueHistory.HISTORY.getLastHistoryItem();
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
	 * Returns number of shown
	 * 
	 * @param rootItem
	 * @return
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

	@Override
	protected void initialized() {
		observeHistoryChange();
		expand(2);
		refreshAll(getRootItem());
	}

	public boolean isShowingDisabled() {
		return showDisabled;
	}

	boolean showDisabled = false;

	public void toggleDisabled() {
		showDisabled = !showDisabled;
		refreshAll(getRootItem());
	}

}
