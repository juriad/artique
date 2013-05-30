package cz.artique.client.sources;

import java.util.ArrayList;
import java.util.Date;
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
import cz.artique.client.manager.AbstractManager;
import cz.artique.client.manager.Manager;
import cz.artique.client.manager.Managers;
import cz.artique.client.service.ClientSourceService;
import cz.artique.client.service.ClientSourceServiceAsync;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.LabelType;
import cz.artique.shared.model.source.Region;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.UserSource;

public class SourcesManager
		extends AbstractManager<ClientSourceServiceAsync>
		implements Manager,
		ProvidesHierarchy<UserSource> {
	public static final SourcesManager MANAGER =
		new SourcesManager();

	private SourcesManager() {
		super(GWT.<ClientSourceServiceAsync> create(ClientSourceService.class));
		refresh(null);
	}

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

	public <T extends Source> void createSource(T source,
			final AsyncCallback<T> ping) {
		service.addSource(source, new AsyncCallback<Source>() {

			public void onFailure(Throwable caught) {
				if (ping != null) {
					ping.onFailure(caught);
				}
			}

			@SuppressWarnings("unchecked")
			public void onSuccess(Source result) {
				if (ping != null) {
					ping.onSuccess((T) result);
				}
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
		return new ArrayList<UserSource>(sourcesNames.values());
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

	public Hierarchy<UserSource> getAdhocItem() {
		return null;
	}

	public void addUserSource(UserSource value,
			final AsyncCallback<UserSource> ping) {
		service.addUserSource(value, new AsyncCallback<UserSource>() {
			public void onFailure(Throwable caught) {
				if (ping != null) {
					ping.onFailure(caught);
				}
			}

			public void onSuccess(UserSource result) {
				sourcesKeys.put(result.getKey(), result);
				sourcesNames.put(result.getName(), result);
				sourcesLabels.put(result.getLabel(), result);

				// first register label, then add hierarchy (it needs label)
				Managers.LABELS_MANAGER.updateUserSourceLabel(result);
				HierarchyUtils.add(hierarchyRoot, result);

				selectSource(result);

				if (ping != null) {
					ping.onSuccess(result);
				}
			}
		});
	}

	public void updateUserSource(UserSource value,
			final AsyncCallback<UserSource> ping) {
		service.updateUserSource(value, new AsyncCallback<UserSource>() {
			public void onFailure(Throwable caught) {
				if (ping != null) {
					ping.onFailure(caught);
				}
			}

			public void onSuccess(UserSource result) {
				UserSource old = sourcesKeys.get(result.getKey());
				sourcesKeys.put(result.getKey(), result);
				sourcesNames.remove(old.getName());
				sourcesNames.put(result.getName(), result);
				sourcesLabels.put(result.getLabel(), result);

				// first register label, then add hierarchy (it needs label)
				Managers.LABELS_MANAGER.updateUserSourceLabel(result);

				HierarchyUtils.remove(hierarchyRoot, old);
				HierarchyUtils.add(hierarchyRoot, result);

				selectSource(result);

				if (ping != null) {
					ping.onSuccess(result);
				}
			}
		});
	}

	protected void selectSource(UserSource result) {
		// TODO nice to have selected source after creating or update
	}

	public void getRegions(Key source, final AsyncCallback<List<Region>> ping) {
		service.getRegions(source, new AsyncCallback<List<Region>>() {

			public void onFailure(Throwable caught) {
				if (ping != null) {
					ping.onFailure(caught);
				}
			}

			public void onSuccess(List<Region> result) {
				if (ping != null) {
					ping.onSuccess(result);
				}
			}
		});
	}

	public void checkRegion(Region region, final AsyncCallback<Region> ping) {
		service.checkRegion(region, new AsyncCallback<Region>() {

			public void onFailure(Throwable caught) {
				if (ping != null) {
					ping.onFailure(caught);
				}
			}

			public void onSuccess(Region result) {
				if (ping != null) {
					ping.onSuccess(result);
				}
			}
		});
	}

	public void planSourceCheck(Key source, final AsyncCallback<Date> ping) {
		service.planSourceCheck(source, new AsyncCallback<Date>() {
			public void onFailure(Throwable caught) {
				if (ping != null) {
					ping.onFailure(caught);
				}
			}

			public void onSuccess(Date result) {
				if (ping != null) {
					ping.onSuccess(result);
				}
			}
		});
	}

}
