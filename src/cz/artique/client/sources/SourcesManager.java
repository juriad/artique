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
import cz.artique.client.manager.Managers;
import cz.artique.client.messages.MessageType;
import cz.artique.client.messages.ValidationMessage;
import cz.artique.client.service.ClientSourceService;
import cz.artique.client.service.ClientSourceService.AddSource;
import cz.artique.client.service.ClientSourceService.AddUserSource;
import cz.artique.client.service.ClientSourceService.CheckRegion;
import cz.artique.client.service.ClientSourceService.GetRecommendation;
import cz.artique.client.service.ClientSourceService.GetRegions;
import cz.artique.client.service.ClientSourceService.GetUserSources;
import cz.artique.client.service.ClientSourceService.PlanSourceCheck;
import cz.artique.client.service.ClientSourceService.UpdateUserSource;
import cz.artique.client.service.ClientSourceServiceAsync;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.LabelType;
import cz.artique.shared.model.recomandation.Recommendation;
import cz.artique.shared.model.source.ManualSource;
import cz.artique.shared.model.source.Region;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.SourceType;
import cz.artique.shared.model.source.UserSource;

/**
 * Manages all existing {@link UserSource} and provides some functionality for
 * {@link Source}s, {@link Region} and {@link Recommendation}s by wrapping
 * {@link ClientSourceService}.
 * 
 * The manager provides the {@link UserSource} in the {@link Hierarchy}.
 * 
 * @author Adam Juraszek
 * 
 */
public class SourcesManager extends AbstractManager<ClientSourceServiceAsync>
		implements ProvidesHierarchy<UserSource> {
	public static final SourcesManager MANAGER = new SourcesManager();

	private SourcesManager() {
		super(GWT.<ClientSourceServiceAsync> create(ClientSourceService.class));
		refresh(null);
	}

	private Map<Key, UserSource> sourcesKeys = new HashMap<Key, UserSource>();
	private Map<Key, UserSource> sourcesLabels = new HashMap<Key, UserSource>();
	private UserSource manualSource;

	/**
	 * Loads list of all existing {@link UserSource}s.
	 * 
	 * @see cz.artique.client.manager.Manager#refresh(com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void refresh(final AsyncCallback<Void> ping) {
		assumeOnline();
		service.getUserSources(new AsyncCallback<List<UserSource>>() {
			public void onFailure(Throwable caught) {
				serviceFailed(caught);
				new ValidationMessage<GetUserSources>(GetUserSources.GENERAL)
					.onFailure(caught);
				if (ping != null) {
					ping.onFailure(caught);
				}
			}

			public void onSuccess(List<UserSource> result) {
				Map<Key, UserSource> newSourcesKeys =
					new HashMap<Key, UserSource>();
				Map<Key, UserSource> newSourcesLabels =
					new HashMap<Key, UserSource>();
				for (UserSource us : result) {
					newSourcesKeys.put(us.getKey(), us);
					newSourcesLabels.put(us.getLabel(), us);
					if (SourceType.MANUAL.equals(us.getSourceType())) {
						manualSource = us;
					}
				}

				updateHierarchy(sourcesKeys, newSourcesKeys);

				sourcesKeys = newSourcesKeys;
				sourcesLabels = newSourcesLabels;

				new ValidationMessage<GetUserSources>(GetUserSources.GENERAL)
					.onSuccess(MessageType.DEBUG);
				if (ping != null) {
					ping.onSuccess(null);
				}

				setReady();
			}
		});
	}

	/**
	 * Creates a new {@link Source} by calling
	 * {@link ClientSourceService#addSource(Source)}
	 * 
	 * @param source
	 *            {@link Source} to be created
	 * @param ping
	 *            callback
	 */
	public <T extends Source> void addSource(T source,
			final AsyncCallback<T> ping) {
		assumeOnline();
		service.addSource(source, new AsyncCallback<Source>() {
			public void onFailure(Throwable caught) {
				serviceFailed(caught);
				new ValidationMessage<AddSource>(AddSource.GENERAL)
					.onFailure(caught);
				if (ping != null) {
					ping.onFailure(caught);
				}
			}

			@SuppressWarnings("unchecked")
			public void onSuccess(Source result) {
				new ValidationMessage<AddSource>(AddSource.GENERAL).onSuccess();
				if (ping != null) {
					ping.onSuccess((T) result);
				}
			}
		});
	}

	/**
	 * Updates hierarchy according to list of old and new {@link UserSource}s.
	 * 
	 * @param sourcesKeys
	 *            list of old {@link UserSource}s
	 * @param newSourcesKeys
	 *            list of new {@link UserSource}s
	 */
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

	/**
	 * @return list of all cached known {@link UserSource}s
	 */
	public List<UserSource> getSources() {
		return new ArrayList<UserSource>(sourcesKeys.values());
	}

	/**
	 * @param key
	 *            key of {@link UserSource}
	 * @return {@link UserSource} defined by its key
	 */
	public UserSource getSourceByKey(Key key) {
		return sourcesKeys.get(key);
	}

	/**
	 * @param label
	 *            {@link Label} of type user-source
	 * @return {@link UserSource} defined by its user-source {@link Label}
	 */
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

	/**
	 * There is no ad-hoc item for {@link UserSource}s.
	 * 
	 * @see cz.artique.client.hierarchy.ProvidesHierarchy#getAdhocItem()
	 */
	public Hierarchy<UserSource> getAdhocItem() {
		return null;
	}

	/**
	 * Creates a new {@link UserSource} by calling
	 * {@link ClientSourceService#addUserSource(UserSource)} and adds it into
	 * the hierarchy.
	 * 
	 * @param value
	 *            {@link UserSource} to be created
	 * @param ping
	 *            callback
	 */
	public void addUserSource(UserSource value,
			final AsyncCallback<UserSource> ping) {
		assumeOnline();
		service.addUserSource(value, new AsyncCallback<UserSource>() {
			public void onFailure(Throwable caught) {
				serviceFailed(caught);
				new ValidationMessage<AddUserSource>(AddUserSource.GENERAL)
					.onFailure(caught);
				if (ping != null) {
					ping.onFailure(caught);
				}
			}

			public void onSuccess(UserSource result) {
				sourcesKeys.put(result.getKey(), result);
				sourcesLabels.put(result.getLabel(), result);

				// first register label, then add hierarchy (it needs label)
				Managers.LABELS_MANAGER.updateUserSourceLabel(result);
				HierarchyUtils.add(hierarchyRoot, result);

				selectSource(result);

				new ValidationMessage<AddUserSource>(AddUserSource.GENERAL)
					.onSuccess();
				if (ping != null) {
					ping.onSuccess(result);
				}
			}
		});
	}

	/**
	 * Updates an existing {@link UserSource} by calling
	 * {@link ClientSourceService#updateUserSource(UserSource)} and updates it
	 * also in the hierarchy.
	 * 
	 * @param value
	 *            {@link UserSource} to be updated
	 * @param ping
	 *            callback
	 */
	public void updateUserSource(UserSource value,
			final AsyncCallback<UserSource> ping) {
		assumeOnline();
		service.updateUserSource(value, new AsyncCallback<UserSource>() {
			public void onFailure(Throwable caught) {
				serviceFailed(caught);
				new ValidationMessage<UpdateUserSource>(
					UpdateUserSource.GENERAL).onFailure(caught);
				if (ping != null) {
					ping.onFailure(caught);
				}
			}

			public void onSuccess(UserSource result) {
				UserSource old = sourcesKeys.get(result.getKey());
				sourcesKeys.put(result.getKey(), result);
				sourcesLabels.put(result.getLabel(), result);

				// first register label, then add hierarchy (it needs label)
				Managers.LABELS_MANAGER.updateUserSourceLabel(result);

				HierarchyUtils.remove(hierarchyRoot, old);
				HierarchyUtils.add(hierarchyRoot, result);

				selectSource(result);

				new ValidationMessage<UpdateUserSource>(
					UpdateUserSource.GENERAL).onSuccess();
				if (ping != null) {
					ping.onSuccess(result);
				}
			}
		});
	}

	/**
	 * Apply filter to created or updated {@link UserSource} for items shown in
	 * main content.
	 * 
	 * @param result
	 *            {@link UserSource} which has been created or updated
	 */
	protected void selectSource(UserSource result) {
		// TODO nice to have: selected source after creating or update
	}

	/**
	 * Gets list of all know {@link Region}s for a {@link Source} by calling
	 * {@link ClientSourceService#getRegions(Key)}.
	 * 
	 * @param source
	 *            {@link Source} the {@link Region}s are gotten for
	 * @param ping
	 *            callback
	 */
	public void getRegions(Key source, final AsyncCallback<List<Region>> ping) {
		assumeOnline();
		service.getRegions(source, new AsyncCallback<List<Region>>() {
			public void onFailure(Throwable caught) {
				serviceFailed(caught);
				new ValidationMessage<GetRegions>(GetRegions.GENERAL)
					.onFailure(caught);
				if (ping != null) {
					ping.onFailure(caught);
				}
			}

			public void onSuccess(List<Region> result) {
				new ValidationMessage<GetRegions>(GetRegions.GENERAL)
					.onSuccess(MessageType.DEBUG);
				if (ping != null) {
					ping.onSuccess(result);
				}
			}
		});
	}

	/**
	 * Checks the {@link Region} by calling
	 * {@link ClientSourceService#checkRegion(Region)}.
	 * 
	 * @param region
	 *            {@link Region} to be checked
	 * @param ping
	 *            callback
	 */
	public void checkRegion(Region region, final AsyncCallback<Region> ping) {
		assumeOnline();
		service.checkRegion(region, new AsyncCallback<Region>() {
			public void onFailure(Throwable caught) {
				serviceFailed(caught);
				new ValidationMessage<CheckRegion>(CheckRegion.GENERAL)
					.onFailure(caught);
				if (ping != null) {
					ping.onFailure(caught);
				}
			}

			public void onSuccess(Region result) {
				new ValidationMessage<CheckRegion>(CheckRegion.GENERAL)
					.onSuccess();
				if (ping != null) {
					ping.onSuccess(result);
				}
			}
		});
	}

	/**
	 * Plans immediate {@link Source} check by calling
	 * {@link ClientSourceService#planSourceCheck(Key)}.
	 * 
	 * @param source
	 *            {@link Source} to be checked
	 * @param ping
	 *            callback
	 */
	public void planSourceCheck(Key source, final AsyncCallback<Date> ping) {
		assumeOnline();
		service.planSourceCheck(source, new AsyncCallback<Date>() {
			public void onFailure(Throwable caught) {
				serviceFailed(caught);
				new ValidationMessage<PlanSourceCheck>(PlanSourceCheck.GENERAL)
					.onFailure(caught);
				if (ping != null) {
					ping.onFailure(caught);
				}
			}

			public void onSuccess(Date result) {
				new ValidationMessage<PlanSourceCheck>(PlanSourceCheck.GENERAL)
					.onSuccess();
				if (ping != null) {
					ping.onSuccess(result);
				}
			}
		});
	}

	/**
	 * Gets list of {@link Source}s recommended to be watched for the user.
	 * 
	 * @param ping
	 *            callback
	 */
	public void getRecommendation(final AsyncCallback<Recommendation> ping) {
		assumeOnline();
		service.getRecommendation(new AsyncCallback<Recommendation>() {
			public void onFailure(Throwable caught) {
				serviceFailed(caught);
				new ValidationMessage<GetRecommendation>(
					GetRecommendation.GENERAL).onFailure(caught);
				if (ping != null) {
					ping.onFailure(caught);
				}
			}

			public void onSuccess(Recommendation result) {
				// do not spam
				// new ValidationMessage<GetRecommandation>(
				// GetRecommandation.GENERAL).onSuccess();
				if (ping != null) {
					ping.onSuccess(result);
				}
			}
		});
	}

	/**
	 * @return the {@link UserSource} of {@link ManualSource}
	 */
	public UserSource getManualSource() {
		return manualSource;
	}

}
