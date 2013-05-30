package cz.artique.client.listFilters;

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
import cz.artique.client.history.HistoryUtils;
import cz.artique.client.i18n.I18n;
import cz.artique.client.i18n.Messages;
import cz.artique.client.manager.AbstractManager;
import cz.artique.client.manager.Managers;
import cz.artique.client.messages.Message;
import cz.artique.client.messages.MessageType;
import cz.artique.client.service.ClientListFilterService;
import cz.artique.client.service.ClientListFilterServiceAsync;
import cz.artique.shared.model.label.ListFilter;

public class ListFiltersManager
		extends AbstractManager<ClientListFilterServiceAsync>
		implements ProvidesHierarchy<ListFilter> {
	public static final ListFiltersManager MANAGER =
		new ListFiltersManager();

	private Hierarchy<ListFilter> hierarchyRoot = HierarchyUtils
		.createRootNode();

	private Hierarchy<ListFilter> adhocItem;

	private ListFiltersManager() {
		super(
			GWT
				.<ClientListFilterServiceAsync> create(ClientListFilterService.class));
		ListFilter adhoc = new ListFilter();
		adhoc.setHierarchy("/");
		adhoc.setName("");
		HierarchyUtils.add(hierarchyRoot, adhoc);
		adhocItem = HierarchyUtils.findInTree(hierarchyRoot, adhoc);
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

	public Hierarchy<ListFilter> getHierarchyRoot() {
		return hierarchyRoot;
	}

	public ListFilter getListFilterByKey(Key key) {
		return listFilterByKey.get(key);
	}

	public void addListFilter(final ListFilter listFilter,
			final AsyncCallback<ListFilter> ping) {
		service.addListFilter(listFilter, new AsyncCallback<ListFilter>() {

			public void onFailure(Throwable caught) {
				Messages messages = I18n.I18N.getMessages();
				Managers.MESSAGES_MANAGER.addMessage(new Message(
					MessageType.ERROR, messages
						.listFilterCreatedError(listFilter.getName())));
				if (ping != null) {
					ping.onFailure(caught);
				}
			}

			public void onSuccess(ListFilter result) {
				listFilterByKey.put(result.getKey(), result);
				HierarchyUtils.add(getHierarchyRoot(), result);
				Messages messages = I18n.I18N.getMessages();
				Managers.MESSAGES_MANAGER.addMessage(new Message(
					MessageType.INFO, messages.listFilterCreated(result
						.getName())));
				String token = HistoryUtils.UTILS.serializeListFilter(result);
				Managers.HISTORY_MANAGER.setListFilter(result, token);
				if (ping != null) {
					ping.onSuccess(result);
				}
			}
		});
	}

	public void deleteListFilter(ListFilter listFilter,
			final AsyncCallback<Void> ping) {
		final ListFilter lf = getListFilterByKey(listFilter.getKey());
		if (lf == null) {
			return;
		}

		service.deleteListFilter(lf, new AsyncCallback<Void>() {

			public void onFailure(Throwable caught) {
				Messages messages = I18n.I18N.getMessages();
				Managers.MESSAGES_MANAGER.addMessage(new Message(
					MessageType.ERROR, messages.listFilterDeletedError(lf
						.getName())));
				if (ping != null) {
					ping.onFailure(caught);
				}
			}

			public void onSuccess(Void result) {
				listFilterByKey.remove(lf.getKey());
				HierarchyUtils.remove(getHierarchyRoot(), lf);
				Messages messages = I18n.I18N.getMessages();
				Managers.MESSAGES_MANAGER.addMessage(new Message(
					MessageType.INFO, messages.listFilterDeleted(lf.getName())));
				if (ping != null) {
					ping.onSuccess(null);
				}
			}
		});
	}

	public void updateListFilter(final ListFilter listFilter,
			final AsyncCallback<ListFilter> ping) {
		service.updateListFilter(listFilter, new AsyncCallback<ListFilter>() {

			public void onFailure(Throwable caught) {
				Messages messages = I18n.I18N.getMessages();
				Managers.MESSAGES_MANAGER.addMessage(new Message(
					MessageType.ERROR, messages
						.listFilterUpdatedError(listFilter.getName())));
				if (ping != null) {
					ping.onFailure(caught);
				}
			}

			public void onSuccess(ListFilter result) {
				HierarchyUtils.remove(hierarchyRoot,
					listFilterByKey.get(result.getKey()));
				HierarchyUtils.add(hierarchyRoot, result);
				listFilterByKey.put(result.getKey(), result);
				Messages messages = I18n.I18N.getMessages();
				Managers.MESSAGES_MANAGER.addMessage(new Message(
					MessageType.INFO, messages.listFilterUpdated(result
						.getName())));
				String token = HistoryUtils.UTILS.serializeListFilter(result);
				Managers.HISTORY_MANAGER.setListFilter(result, token);
				if (ping != null) {
					ping.onSuccess(null);
				}
			}
		});
	}

	public Hierarchy<ListFilter> getAdhocItem() {
		return adhocItem;
	}
}
