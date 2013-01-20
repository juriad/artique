package cz.artique.client.service;

import java.util.Map;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.artique.shared.items.ChangeSet;
import cz.artique.shared.items.ListingUpdate;
import cz.artique.shared.items.ListingUpdateRequest;
import cz.artique.shared.model.item.ManualItem;
import cz.artique.shared.model.item.UserItem;

@RemoteServiceRelativePath("service.s3gwt")
public interface ClientItemService extends RemoteService {
	ListingUpdate<UserItem> getItems(ListingUpdateRequest request);

	UserItem addItem(ManualItem item);

	Map<Key, UserItem> updateItems(Map<Key, ChangeSet> changeSets);
}
