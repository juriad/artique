package cz.artique.client.artiqueItems;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.AbstractManager;
import cz.artique.client.artiqueLabels.ArtiqueLabelsManager;
import cz.artique.client.items.ItemsManager;
import cz.artique.client.service.ClientItemService;
import cz.artique.client.service.ClientItemServiceAsync;
import cz.artique.shared.list.ListingUpdate;
import cz.artique.shared.list.ListingUpdateRequest;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.Label;

public class ArtiqueItemsManager
		extends AbstractManager<ClientItemServiceAsync>
		implements ItemsManager<UserItem, Label> {
	public static final ArtiqueItemsManager MANAGER = new ArtiqueItemsManager();

	private ArtiqueItemsManager() {
		super(ClientItemService.class);
	}

	// TODO posílat balíky změn
	
	public void labelAdded(UserItem userItem, Label label,
			AsyncCallback<Void> ping) {
		if (userItem.getLabels().contains(label.getKey())) {
			ping.onSuccess(null);
		} else {
			userItem.getLabels().add(label.getKey());
			service.updateUserItem(userItem, ping);
		}
	}

	public void labelRemoved(UserItem userItem, Label label,
			AsyncCallback<Void> ping) {
		if (!userItem.getLabels().remove(label.getKey())) {
			ping.onSuccess(null);
		} else {
			service.updateUserItem(userItem, ping);
		}
	}

	public void readSet(UserItem userItem, boolean read,
			AsyncCallback<Void> ping) {
		if (read == userItem.isRead()) {
			ping.onSuccess(null);
		} else {
			userItem.setRead(read);
			service.updateUserItem(userItem, ping);
		}
	}

	public void refresh(AsyncCallback<Void> ping) {
		// nonsence in this context
	}

	public void getItems(final ListingUpdateRequest request,
			final AsyncCallback<ListingUpdate<UserItem>> ping) {
		ArtiqueLabelsManager.MANAGER.ready(new AsyncCallback<Void>() {

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
}
