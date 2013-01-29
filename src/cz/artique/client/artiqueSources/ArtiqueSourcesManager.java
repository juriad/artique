package cz.artique.client.artiqueSources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import com.google.appengine.api.datastore.Key;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.hierarchy.Hierarchy;
import cz.artique.client.hierarchy.HierarchyUtils;
import cz.artique.client.manager.AbstractManager;
import cz.artique.client.service.ClientSourceService;
import cz.artique.client.service.ClientSourceServiceAsync;
import cz.artique.client.sources.SourcesManager;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.LabelType;
import cz.artique.shared.model.source.UserSource;

public class ArtiqueSourcesManager
		extends AbstractManager<ClientSourceServiceAsync>
		implements SourcesManager<UserSource, Key>,
		ProvidesHierarchy<UserSource> {
	public static final ArtiqueSourcesManager MANAGER =
		new ArtiqueSourcesManager();

	private ArtiqueSourcesManager() {
		super(GWT.<ClientSourceServiceAsync> create(ClientSourceService.class));
		refresh(null);
	}

	private List<UserSource> sources = new ArrayList<UserSource>();
	private Map<Key, UserSource> sourcesKeys = new HashMap<Key, UserSource>();
	private Map<String, UserSource> sourcesNames =
		new HashMap<String, UserSource>();
	private Map<Key, UserSource> sourcesLabels = new HashMap<Key, UserSource>();

	public void refresh(final AsyncCallback<Void> ping) {
		service.getUserSources(new AsyncCallback<List<UserSource>>() {

			public void onFailure(Throwable caught) {
				if (ping != null) {
					ping.onFailure(caught);
				}
			}

			public void onSuccess(List<UserSource> result) {
				Map<Key, UserSource> newSourcesKeys =
					new HashMap<Key, UserSource>();
				Map<String, UserSource> newSourcesNames =
					new HashMap<String, UserSource>();
				Map<Key, UserSource> newSourcesLabels =
					new HashMap<Key, UserSource>();
				for (UserSource us : result) {
					newSourcesKeys.put(us.getKey(), us);
					newSourcesNames.put(us.getName(), us);
					newSourcesLabels.put(us.getLabel(), us);
				}

				updateHierarchy(sourcesKeys, newSourcesKeys);

				sourcesKeys = newSourcesKeys;
				sourcesNames = newSourcesNames;
				sourcesLabels = newSourcesLabels;

				if (ping != null) {
					ping.onSuccess(null);
				}
				setReady();
			}
		});
	}

	private void updateHierarchy(Map<Key, UserSource> sourcesKeys,
			Map<Key, UserSource> newSourcesKeys) {
		Set<Key> keys = new HashSet<Key>();
		keys.addAll(sourcesKeys.keySet());
		keys.addAll(newSourcesKeys.keySet());

		for (Key key : keys) {
			UserSource inOld = sourcesKeys.get(key);
			UserSource inNew = newSourcesKeys.get(key);
			System.out.println("update hierarchy:");
			System.out.println(key);
			System.out.println(inOld);
			System.out.println(inNew);

			if (inOld == null && inNew != null) {
				// added
				HierarchyUtils.add(hierarchyRoot, inNew);
			} else if (inOld != null && inNew == null) {
				// removed
				HierarchyUtils.remove(hierarchyRoot, inOld);
			} else {
				// exists in both
				if (inOld.getHierarchy().equals(inNew.getHierarchy())) {
					// hierarchy is ok
					if (!inOld.equalsDeeply(inNew)) {
						if (inOld.getName().equals(inNew.getName())) {
							Hierarchy<UserSource> inTree =
								HierarchyUtils.findInTree(hierarchyRoot, inNew);
							inTree.fireChanged();
						} else {
							HierarchyUtils.remove(hierarchyRoot, inOld);
							HierarchyUtils.add(hierarchyRoot, inNew);
						}
					}
				} else {
					HierarchyUtils.remove(hierarchyRoot, inOld);
					HierarchyUtils.add(hierarchyRoot, inNew);
				}
			}
		}
	}

	public List<UserSource> getSources() {
		return sources;
	}

	public UserSource getSourceByName(String name) {
		return sourcesNames.get(name);
	}

	public UserSource getSourceByKey(Key key) {
		return sourcesKeys.get(key);
	}

	public UserSource getByLabel(Label label) {
		if (!LabelType.USER_SOURCE.equals(label.getLabelType())) {
			return null;
		}
		return sourcesLabels.get(label.getKey());
	}

	private Hierarchy<UserSource> hierarchyRoot = HierarchyUtils
		.createRootNode();

	public Hierarchy<UserSource> getHierarchyRoot() {
		return hierarchyRoot;
	}

}
