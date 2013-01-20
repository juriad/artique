package cz.artique.client.service;

import java.util.Map;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.shared.items.ChangeSet;
import cz.artique.shared.items.ListingUpdate;
import cz.artique.shared.items.ListingUpdateRequest;
import cz.artique.shared.model.item.ManualItem;
import cz.artique.shared.model.item.UserItem;

public interface ClientItemServiceAsync {

	void getItems(ListingUpdateRequest request,
			AsyncCallback<ListingUpdate<UserItem>> callback);

	void addItem(ManualItem item, AsyncCallback<UserItem> callback);

	void updateItems(Map<Key, ChangeSet> changeSets,
			AsyncCallback<Map<Key, UserItem>> callback);

}
