package cz.artique.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.artique.shared.list.ListingUpdate;
import cz.artique.shared.list.ListingUpdateRequest;
import cz.artique.shared.model.item.ManualItem;
import cz.artique.shared.model.item.UserItem;

@RemoteServiceRelativePath("service.s3gwt")
public interface ClientItemService extends RemoteService {
	ListingUpdate<UserItem> getItems(ListingUpdateRequest request);

	UserItem addItem(ManualItem item);

	UserItem updateUserItem(UserItem item);
}
