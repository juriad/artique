package cz.artique.client.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.history.HistoryEvent;
import cz.artique.client.history.HistoryHandler;
import cz.artique.client.history.HistoryManager;
import cz.artique.client.manager.AbstractManager;
import cz.artique.client.manager.ManagerReady;
import cz.artique.client.manager.Managers;
import cz.artique.client.messages.ValidationMessage;
import cz.artique.client.service.ClientItemService;
import cz.artique.client.service.ClientItemService.AddManualItem;
import cz.artique.client.service.ClientItemService.GetItems;
import cz.artique.client.service.ClientItemService.UpdateItems;
import cz.artique.client.service.ClientItemServiceAsync;
import cz.artique.shared.items.ChangeSet;
import cz.artique.shared.items.ListingRequest;
import cz.artique.shared.items.ListingResponse;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.ListFilter;

/**
 * Items manager wraps {@link ClientItemService}; provides its methods.
 * It also manages sets of changes for of {@link UserItem} attributes and sends
 * them periodically to server.
 * 
 * @author Adam Juraszek
 * 
 */
public class ItemsManager extends AbstractManager<ClientItemServiceAsync>
		implements HasModifiedHandlers {
	public static final ItemsManager MANAGER = new ItemsManager();

	private Map<Key, ChangeSet> changeSets;
	private Map<Key, AsyncCallback<UserItem>> pings;

	private final Timer timer;

	/**
	 * Send all pending change sets when history changes and also each 3
	 * seconds.
	 */
	private ItemsManager() {
		super(GWT.<ClientItemServiceAsync> create(ClientItemService.class));
		changeSets = new HashMap<Key, ChangeSet>();
		pings = new HashMap<Key, AsyncCallback<UserItem>>();

		timer = new Timer() {
			@Override
			public void run() {
				refresh(null);
			}
		};

		// TODO nice to have: configure timeout for
		timer.scheduleRepeating(Math.max(getTimeout(), 3000));
		HistoryManager.HISTORY.addHistoryHandler(new HistoryHandler() {
			public void onHistoryChanged(HistoryEvent e) {
				refresh(null);
			}
		}, 1);
	}

	/**
	 * Get existing or create new {@link ChangeSet} for {@link UserItem}.
	 * 
	 * @param userItem
	 *            {@link UserItem}
	 * @return existing or new {@link ChangeSet}
	 */
	private ChangeSet getChangeSet(UserItem userItem) {
		ChangeSet changeSet = changeSets.get(userItem.getKey());
		if (changeSet == null) {
			changeSet = new ChangeSet(userItem.getKey(), userItem.isRead());
			changeSets.put(userItem.getKey(), changeSet);
		}
		return changeSet;
	}

	/**
	 * Informs manager about {@link Label} to be added to a {@link UserItem}.
	 * The {@link Label} will be added to {@link ChangeSet}.
	 * 
	 * @param userItem
	 *            {@link UserItem} to be changed
	 * @param label
	 *            label which was added
	 * @param ping
	 *            callback when applied
	 */
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

	/**
	 * Informs manager about {@link Label} to be removed from a {@link UserItem}
	 * .
	 * The {@link Label} will be added to {@link ChangeSet}.
	 * 
	 * @param userItem
	 *            {@link UserItem} to be changed
	 * @param label
	 *            label which was removed
	 * @param ping
	 *            callback when applied
	 */
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

	/**
	 * Informs manager about new read state of a {@link UserItem}.
	 * The new state will be added to {@link ChangeSet}.
	 * 
	 * @param userItem
	 *            {@link UserItem} to be changed
	 * @param read
	 *            new read state
	 * @param ping
	 *            callback when applied
	 */
	public synchronized void readSet(UserItem userItem, boolean read,
			AsyncCallback<UserItem> ping) {
		if (read == userItem.isRead()) {
			if (ping != null) {
				ping.onSuccess(userItem);
			}
		} else {
			ChangeSet changeSet = getChangeSet(userItem);
			userItem.setRead(read);
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

	/**
	 * Send all pending {@link ChangeSet}s to server to be performed.
	 * 
	 * @see cz.artique.client.manager.Manager#refresh(com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public synchronized void refresh(final AsyncCallback<Void> ping) {
		if (changeSets.isEmpty()) {
			return;
		}

		final Map<Key, ChangeSet> copyChangeSets = changeSets;
		changeSets = new HashMap<Key, ChangeSet>();
		final Map<Key, AsyncCallback<UserItem>> copyPings = pings;
		pings = new HashMap<Key, AsyncCallback<UserItem>>();

		assumeOnline();
		service.updateItems(copyChangeSets,
			new AsyncCallback<Map<Key, UserItem>>() {
				public void onFailure(Throwable caught) {
					serviceFailed(caught);
					new ValidationMessage<UpdateItems>(UpdateItems.GENERAL)
						.onFailure(caught);
					mergeFailed(copyChangeSets, copyPings);
				}

				public void onSuccess(Map<Key, UserItem> result) {
					boolean someFailed = false;
					for (Key key : copyChangeSets.keySet()) {
						AsyncCallback<UserItem> ping = pings.get(key);
						if (ping != null) {
							if (result.containsKey(key)) {
								pings.get(key).onSuccess(result.get(key));
							} else {
								someFailed = true;
								pings.get(key).onFailure(null);
							}
						}
					}
					if (someFailed) {
						new ValidationMessage<UpdateItems>(UpdateItems.GENERAL)
							.onFailure(new RuntimeException(
								"force show error even if some passed"));
					} else {
						// do not spam in messages
						// new
						// ValidationMessage<UpdateItems>(UpdateItems.GENERAL)
						// .onSuccess(MessageType.DEBUG);
					}

					if (ping != null) {
						ping.onSuccess(null);
					}
					fireModifiedEvent(new ModifiedEvent(result));
				}
			});
	}

	/**
	 * Reapplies failed changes to current {@link ChangeSet}s.
	 * 
	 * @param copyChangeSets
	 *            failed {@link ChangeSet}s
	 * @param copyPings
	 *            callbacks of failed changes
	 */
	private synchronized void mergeFailed(Map<Key, ChangeSet> copyChangeSets,
			Map<Key, AsyncCallback<UserItem>> copyPings) {
		for (Key key : copyChangeSets.keySet()) {
			ChangeSet changeSet = changeSets.get(key);
			if (changeSet == null) {
				// dummy read
				changeSet = copyChangeSets.get(key);
				changeSets.put(key, changeSet);
			} else {
				// TODO nice to have: replay changeSet when failed
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

	/**
	 * Just calls {@link ClientItemService#getItems(ListingRequest)} method.
	 * 
	 * @param request
	 *            request containing information about {@link ListFilter} and
	 *            items range
	 * @param ping
	 *            callback
	 */
	public void getItems(final ListingRequest request,
			final AsyncCallback<ListingResponse> ping) {
		assumeOnline();
		service.getItems(request, new AsyncCallback<ListingResponse>() {
			public void onFailure(Throwable caught) {
				serviceFailed(caught);
				new ValidationMessage<GetItems>(GetItems.GENERAL)
					.onFailure(caught);
				if (ping != null) {
					ping.onFailure(caught);
				}
			}

			public void onSuccess(ListingResponse result) {
				// don't spam in messages
				// new ValidationMessage<GetItems>(GetItems.GENERAL)
				// .onSuccess(MessageType.DEBUG);
				if (ping != null) {
					ping.onSuccess(result);
				}
			}
		});
	}

	/**
	 * Manager is ready immediately.
	 * 
	 * @see cz.artique.client.manager.AbstractManager#ready(cz.artique.client.manager.ManagerReady)
	 */
	@Override
	public void ready(ManagerReady ping) {
		Managers.LABELS_MANAGER.ready(ping);
	}

	private final List<ModifiedHandler> handlers =
		new ArrayList<ModifiedHandler>();

	/**
	 * Notify handlers about event.
	 * 
	 * @param event
	 *            {@link ModifiedEvent} to be fired
	 */
	private void fireModifiedEvent(ModifiedEvent event) {
		for (ModifiedHandler h : handlers) {
			h.onModified(event);
		}
	}

	public HandlerRegistration addModifiedHandler(final ModifiedHandler handler) {
		handlers.add(handler);
		return new HandlerRegistration() {
			public void removeHandler() {
				handlers.remove(handler);
			}
		};
	}

	/**
	 * Just calls {@link ClientItemService#addManualItem(UserItem)} method.
	 * 
	 * @param item
	 *            new manual item to add
	 * @param ping
	 *            callback
	 */
	public void addManualItem(UserItem item, final AsyncCallback<UserItem> ping) {
		assumeOnline();
		service.addManualItem(item, new AsyncCallback<UserItem>() {
			public void onFailure(Throwable caught) {
				serviceFailed(caught);
				new ValidationMessage<AddManualItem>(AddManualItem.GENERAL)
					.onFailure(caught);
				if (ping != null) {
					ping.onFailure(caught);
				}
			}

			public void onSuccess(UserItem result) {
				new ValidationMessage<AddManualItem>(AddManualItem.GENERAL)
					.onSuccess();
				if (ping != null) {
					ping.onSuccess(result);
				}
			}
		});
	}
}
