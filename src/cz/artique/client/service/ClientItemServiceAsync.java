package cz.artique.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.shared.list.ListingUpdate;
import cz.artique.shared.list.ListingUpdateRequest;
import cz.artique.shared.model.item.ManualItem;
import cz.artique.shared.model.item.UserItem;

public interface ClientItemServiceAsync {

	void getItems(ListingUpdateRequest request,
			AsyncCallback<ListingUpdate<UserItem>> callback);

	void addItem(ManualItem item, AsyncCallback<UserItem> callback);

	void updateUserItem(UserItem item, AsyncCallback<UserItem> callback);

}
