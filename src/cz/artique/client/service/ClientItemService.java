package cz.artique.client.service;

import java.util.Map;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.artique.shared.items.ChangeSet;
import cz.artique.shared.items.ListingResponse;
import cz.artique.shared.items.ListingRequest;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.validation.ValidationException;

@RemoteServiceRelativePath("service.s3gwt")
public interface ClientItemService extends RemoteService {
	ListingResponse<UserItem> getItems(ListingRequest request) throws ValidationException;

	UserItem addManualItem(UserItem item);

	Map<Key, UserItem> updateItems(Map<Key, ChangeSet> changeSets);
}
