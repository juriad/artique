package cz.artique.client.artiqueHierarchy;

import cz.artique.client.artiqueSources.ArtiqueSourcesManager;
import cz.artique.shared.model.source.UserSource;

public class ArtiqueSourcesTree extends AbstractHierarchyTree<UserSource> {

	public ArtiqueSourcesTree() {
		super(ArtiqueSourcesManager.MANAGER.getHierarchyRoot(),
			UserSourceTreeItemFactory.factory);
		// TODO tady pokracovat
	}

}
