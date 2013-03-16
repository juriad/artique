package cz.artique.client.artiqueListFilters;

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
import cz.artique.client.hierarchy.ProvidesHierarchy;
import cz.artique.client.listFilters.ListFiltersManager;
import cz.artique.client.manager.AbstractManager;
import cz.artique.client.manager.Managers;
import cz.artique.client.service.ClientListFilterService;
import cz.artique.client.service.ClientListFilterServiceAsync;
import cz.artique.shared.model.label.ListFilter;

public class ArtiqueListFiltersManager
		extends AbstractManager<ClientListFilterServiceAsync>
		implements ListFiltersManager<ListFilter>,
		ProvidesHierarchy<ListFilter> {
	public static final ArtiqueListFiltersManager MANAGER =
		new ArtiqueListFiltersManager();

	private ArtiqueListFiltersManager() {
		super(
			GWT
				.<ClientListFilterServiceAsync> create(ClientListFilterService.class));
		refresh(null);
	}

	private Map<Key, ListFilter> listFilterByKey =
		new HashMap<Key, ListFilter>();

	public void refresh(final AsyncCallback<Void> ping) {
		service.getAllListFilters(new AsyncCallback<List<ListFilter>>() {

			public void onFailure(Throwable caught) {
				if (ping != null) {
					ping.onFailure(caught);
				}
			}

			public void onSuccess(List<ListFilter> result) {
				Map<Key, ListFilter> oldByKey = listFilterByKey;
				listFilterByKey = new HashMap<Key, ListFilter>();
				for (ListFilter lf : result) {
					listFilterByKey.put(lf.getKey(), lf);
				}

				updateHierarchy(oldByKey, listFilterByKey);

				if (ping != null) {
					ping.onSuccess(null);
				}

				Managers.waitForManagers(new AsyncCallback<Void>() {
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
					}

					public void onSuccess(Void result) {
						setReady();
					}
				}, Managers.LABELS_MANAGER);
			}
		});
	}

	private void updateHierarchy(Map<Key, ListFilter> oldByKey,
			Map<Key, ListFilter> newByKey) {
		Set<Key> keys = new HashSet<Key>();
		keys.addAll(oldByKey.keySet());
		keys.addAll(newByKey.keySet());

		for (Key key : keys) {
			ListFilter inOld = oldByKey.get(key);
			ListFilter inNew = newByKey.get(key);

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
							Hierarchy<ListFilter> inTree =
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

	private Hierarchy<ListFilter> hierarchyRoot = HierarchyUtils
		.createRootNode();

	public Hierarchy<ListFilter> getHierarchyRoot() {
		return hierarchyRoot;
	}

	public void addListFilter(ListFilter listFilter,
			final AsyncCallback<ListFilter> ping) {
		service.addListFilter(listFilter, new AsyncCallback<ListFilter>() {

			public void onFailure(Throwable caught) {
				if (ping != null) {
					ping.onFailure(caught);
				}
			}

			public void onSuccess(ListFilter result) {
				listFilterByKey.put(result.getKey(), result);
				HierarchyUtils.add(getHierarchyRoot(), result);
				if (ping != null) {
					ping.onSuccess(result);
				}
			}
		});
	}

	public void deleteListFilter(final ListFilter listFilter,
			final AsyncCallback<Void> ping) {
		service.deleteListFilter(listFilter, new AsyncCallback<Void>() {

			public void onFailure(Throwable caught) {
				if (ping != null) {
					ping.onFailure(caught);
				}
			}

			public void onSuccess(Void result) {
				listFilterByKey.remove(listFilter.getKey());
				HierarchyUtils.remove(getHierarchyRoot(), listFilter);
				if (ping != null) {
					ping.onSuccess(null);
				}
			}
		});
	}

	public void updateListFilter(ListFilter listFilter,
			final AsyncCallback<ListFilter> ping) {
		service.updateListFilter(listFilter, new AsyncCallback<ListFilter>() {

			public void onFailure(Throwable caught) {
				if (ping != null) {
					ping.onFailure(caught);
				}
			}

			public void onSuccess(ListFilter result) {
				HierarchyUtils.remove(hierarchyRoot,
					listFilterByKey.get(result.getKey()));
				HierarchyUtils.add(hierarchyRoot, result);
				listFilterByKey.put(result.getKey(), result);
			}
		});
	}
}
