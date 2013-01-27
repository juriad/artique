package cz.artique.client.artiqueHierarchy;

import cz.artique.client.artiqueSources.ArtiqueSourcesManager;
import cz.artique.shared.model.source.UserSource;

public class ArtiqueSourcesTree
		extends AbstractHierarchyTree<UserSource, ArtiqueSourcesManager> {

	public ArtiqueSourcesTree() {
		super(ArtiqueSourcesManager.MANAGER, UserSourceTreeItemFactory.factory);
	}

}
