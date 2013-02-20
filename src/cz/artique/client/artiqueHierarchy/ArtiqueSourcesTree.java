package cz.artique.client.artiqueHierarchy;

import com.google.gwt.user.client.ui.TreeItem;

import cz.artique.client.artiqueHierarchy.UserSourceWidget.UserSourceWidgetFactory;
import cz.artique.client.artiqueHistory.ArtiqueHistory;
import cz.artique.client.artiqueHistory.HistoryEvent;
import cz.artique.client.artiqueHistory.HistoryHandler;
import cz.artique.client.artiqueSources.ArtiqueSourcesManager;
import cz.artique.client.hierarchy.HierarchyTreeWidget;
import cz.artique.shared.model.source.UserSource;

public class ArtiqueSourcesTree
		extends AbstractHierarchyTree<UserSource, ArtiqueSourcesManager> {

	public ArtiqueSourcesTree() {
		super(ArtiqueSourcesManager.MANAGER, UserSourceWidgetFactory.FACTORY);
		observeHistoryChange();
	}

	private void observeHistoryChange() {
		ArtiqueHistory.HISTORY.addHistoryHandler(new HistoryHandler() {
			public void onHistoryChanged(HistoryEvent e) {
				TreeItem rootItem = getRootItem();
				if (rootItem == null) {
					return;
				}

				refreshAll(rootItem);
			}
		});
	}

	protected void refreshAll(TreeItem rootItem) {
		HierarchyTreeWidget<UserSource> w = getHierarchyWidget(rootItem);
		w.refresh();
		for (int i = 0; i < rootItem.getChildCount(); i++) {
			refreshAll(rootItem.getChild(i));
		}
	}

}
