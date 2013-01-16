package cz.artique.client.artiqueItems;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import cz.artique.client.RpcWithTimeoutRequestBuilder;
import cz.artique.client.items.ItemsManager;
import cz.artique.client.service.ClientItemService;
import cz.artique.client.service.ClientItemServiceAsync;
import cz.artique.shared.list.ListingUpdate;
import cz.artique.shared.list.ListingUpdateRequest;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.Label;

public enum ArtiqueItemsManager implements ItemsManager<UserItem, Label> {
	MANAGER;

	private final ClientItemServiceAsync cis;
	private int timeout;

	private ArtiqueItemsManager() {
		cis = GWT.create(ClientItemService.class);
		this.timeout = 0;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
		((ServiceDefTarget) cis)
			.setRpcRequestBuilder(new RpcWithTimeoutRequestBuilder(getTimeout()));
	}

	public int getTimeout() {
		return timeout;
	}

	public void labelAdded(UserItem userItem, Label label,
			AsyncCallback<Void> ping) {
		if (userItem.getLabels().contains(label.getKey())) {
			ping.onSuccess(null);
		} else {
			userItem.getLabels().add(label.getKey());
			cis.updateUserItem(userItem, ping);
		}
	}

	public void labelRemoved(UserItem userItem, Label label,
			AsyncCallback<Void> ping) {
		if (!userItem.getLabels().remove(label.getKey())) {
			ping.onSuccess(null);
		} else {
			userItem.getLabels().add(label.getKey());
			cis.updateUserItem(userItem, ping);
		}
	}

	public void readSet(UserItem userItem, boolean read,
			AsyncCallback<Void> ping) {
		if (read == userItem.isRead()) {
			ping.onSuccess(null);
		} else {
			userItem.setRead(read);
			cis.updateUserItem(userItem, ping);
		}
	}

	public void refresh(AsyncCallback<Void> ping) {
		// nonsence in this context
	}

	public void getItems(ListingUpdateRequest request,
			AsyncCallback<ListingUpdate<UserItem>> ping) {
		cis.getItems(request, ping);
	}
}
