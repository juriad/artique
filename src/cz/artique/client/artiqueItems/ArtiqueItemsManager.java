package cz.artique.client.artiqueItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.artiqueHistory.ArtiqueHistory;
import cz.artique.client.artiqueHistory.HistoryEvent;
import cz.artique.client.artiqueHistory.HistoryHandler;
import cz.artique.client.items.ItemsManager;
import cz.artique.client.manager.AbstractManager;
import cz.artique.client.manager.Managers;
import cz.artique.client.service.ClientItemService;
import cz.artique.client.service.ClientItemServiceAsync;
import cz.artique.shared.items.ChangeSet;
import cz.artique.shared.items.ListingUpdate;
import cz.artique.shared.items.ListingUpdateRequest;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.Label;

public class ArtiqueItemsManager
		extends AbstractManager<ClientItemServiceAsync>
		implements ItemsManager<UserItem, Label>, HasModifiedHandlers {
	public static final ArtiqueItemsManager MANAGER = new ArtiqueItemsManager();

	private Map<Key, ChangeSet> changeSets;
	private Map<Key, AsyncCallback<UserItem>> pings;

	private final Timer timer;

	private ArtiqueItemsManager() {
		super(GWT.<ClientItemServiceAsync> create(ClientItemService.class));
		changeSets = new HashMap<Key, ChangeSet>();
		pings = new HashMap<Key, AsyncCallback<UserItem>>();

		timer = new Timer() {

			@Override
			public void run() {
				refresh(null);
			}
		};

		// TODO timer settings: 3 seconds
		timer.scheduleRepeating(Math.max(getTimeout(), 3000));

		ArtiqueHistory.HISTORY.addHistoryHandler(new HistoryHandler() {

			public void onHistoryChanged(HistoryEvent e) {
				refresh(null);
			}
		}, 1);
	}

	private ChangeSet getChangeSet(UserItem userItem) {
		ChangeSet changeSet = changeSets.get(userItem.getKey());
		if (changeSet == null) {
			changeSet = new ChangeSet(userItem.getKey(), userItem.isRead());
			changeSets.put(userItem.getKey(), changeSet);
		}
		return changeSet;
	}

	public synchronized void labelAdded(UserItem userItem, Label label,
			AsyncCallback<UserItem> ping) {
		if (userItem.getLabels().contains(label.getKey())) {
			// did contain
			if (ping != null) {
				ping.onSuccess(userItem);
			}
		} else {
			userItem.getLabels().add(label.getKey());
			if (getChangeSet(userItem).addLabel(label.getKey())) {
				// enqued
				if (ping != null) {
					pings.put(userItem.getKey(), ping);
				}
			} else {
				if (ping != null) {
					ping.onSuccess(userItem);
				}
			}
		}
	}

	public synchronized void labelRemoved(UserItem userItem, Label label,
			AsyncCallback<UserItem> ping) {
		if (!userItem.getLabels().remove(label.getKey())) {
			// did not contain
			if (ping != null) {
				ping.onSuccess(userItem);
			}
		} else {
			if (getChangeSet(userItem).removeLabel(label.getKey())) {
				// enqued
				if (ping != null) {
					pings.put(userItem.getKey(), ping);
				}
			} else {
				if (ping != null) {
					ping.onSuccess(userItem);
				}
			}
		}
	}

	public synchronized void readSet(UserItem userItem, boolean read,
			AsyncCallback<UserItem> ping) {
		if (read == userItem.isRead()) {
			if (ping != null) {
				ping.onSuccess(userItem);
			}
		} else {
			userItem.setRead(read);
			ChangeSet changeSet = getChangeSet(userItem);
			if (changeSet.setRead(read)) {
				if (ping != null) {
					pings.put(userItem.getKey(), ping);
				}
			} else {
				if (ping != null) {
					ping.onSuccess(userItem);
				}
			}
		}
	}

	public synchronized void refresh(final AsyncCallback<Void> ping) {
		if (changeSets.isEmpty()) {
			return;
		}

		GWT.log("sending change set");

		final Map<Key, ChangeSet> copyChangeSets = changeSets;
		changeSets = new HashMap<Key, ChangeSet>();
		final Map<Key, AsyncCallback<UserItem>> copyPings = pings;
		pings = new HashMap<Key, AsyncCallback<UserItem>>();

		service.updateItems(copyChangeSets,
			new AsyncCallback<Map<Key, UserItem>>() {

				public void onFailure(Throwable caught) {
					GWT.log("change set error: ", caught);
					// TODO inform about failure
					mergeFailed(copyChangeSets, copyPings);
				}

				public void onSuccess(Map<Key, UserItem> result) {
					GWT.log("change set result");
					for (Key key : copyChangeSets.keySet()) {
						AsyncCallback<UserItem> ping = pings.get(key);
						if (ping != null) {
							if (result.containsKey(key)) {
								pings.get(key).onSuccess(result.get(key));
							} else {
								// TODO inform about failure
								pings.get(key).onFailure(null);
							}
						}
					}
					if (ping != null) {
						ping.onSuccess(null);
					}
					fireModifiedEvent(new ModifiedEvent(result));

				}
			});
	}

	private synchronized void mergeFailed(Map<Key, ChangeSet> copyChangeSets,
			Map<Key, AsyncCallback<UserItem>> copyPings) {
		for (Key key : copyChangeSets.keySet()) {
			ChangeSet changeSet = changeSets.get(key);
			if (changeSet == null) {
				// dummy read
				changeSet = copyChangeSets.get(key);
				changeSets.put(key, changeSet);
			} else {
				// XXX replay changeSet onto failed?
				ChangeSet failed = copyChangeSets.get(key);
				for (Key al : failed.getLabelsAdded()) {
					changeSet.addLabel(al);
				}
				for (Key rl : failed.getLabelsRemoved()) {
					changeSet.removeLabel(rl);
				}
				if (changeSet.getReadState() == null) {
					changeSet.setReadState(failed.getReadState());
				}
			}
		}
	}

	public void getItems(final ListingUpdateRequest request,
			final AsyncCallback<ListingUpdate<UserItem>> ping) {
		Managers.LABELS_MANAGER.ready(new AsyncCallback<Void>() {

			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			public void onSuccess(Void result) {
				GWT.log("get items");
				service.getItems(request, ping);
			}
		});
	}

	public void ready(AsyncCallback<Void> ping) {
		ping.onSuccess(null);
	}

	private final List<ModifiedHandler> handlers =
		new ArrayList<ModifiedHandler>();

	private void fireModifiedEvent(ModifiedEvent event) {
		for (ModifiedHandler h : handlers) {
			h.onModified(event);
		}
	}

	public HandlerRegistration addGeneralClickHandler(
			final ModifiedHandler handler) {
		handlers.add(handler);
		return new HandlerRegistration() {

			public void removeHandler() {
				handlers.remove(handler);
			}
		};
	}
}
