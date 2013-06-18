package cz.artique.client.service;

import java.util.Map;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.shared.items.ChangeSet;
import cz.artique.shared.items.ListingRequest;
import cz.artique.shared.items.ListingResponse;
import cz.artique.shared.model.item.UserItem;

public interface ClientItemServiceAsync {

	void getItems(ListingRequest request,
			AsyncCallback<ListingResponse> callback);

	void updateItems(Map<Key, ChangeSet> changeSets,
			AsyncCallback<Map<Key, UserItem>> callback);

	void addManualItem(UserItem item, AsyncCallback<UserItem> callback);

}
