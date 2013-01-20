package cz.artique.server.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import cz.artique.client.service.ClientItemService;
import cz.artique.shared.items.ChangeSet;
import cz.artique.shared.items.ListingUpdate;
import cz.artique.shared.items.ListingUpdateRequest;
import cz.artique.shared.model.item.ContentType;
import cz.artique.shared.model.item.ManualItem;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.utils.PropertyEmptyException;
import cz.artique.shared.utils.PropertyTooLongException;
import cz.artique.shared.utils.SecurityBreachException;

public class ClientItemServiceImpl implements ClientItemService {

	public ListingUpdate<UserItem> getItems(ListingUpdateRequest request) {
		// TODO sanitize getItems
		ItemService is = new ItemService();
		User user = UserServiceFactory.getUserService().getCurrentUser();
		return is.getItems(user, request);
	}

	public UserItem addItem(ManualItem item)
			throws NullPointerException, PropertyTooLongException,
			PropertyEmptyException, SecurityBreachException {
		if (item == null) {
			throw new NullPointerException("Adding manual item may not be null");
		}
		Sanitizer.checkStringEmpty("title", item.getTitle());
		Sanitizer.checkTextEmpty("content", item.getContent());
		Sanitizer.checkUrl("url", item.getUrl());
		item.setContent(Sanitizer.trimText(item.getContent()));
		item.setContentType(ContentType.PLAIN_TEXT);
		item.setAdded(new Date());
		item.setPublished(null);
		item.setHash(null);

		ItemService is = new ItemService();
		return is.addManualItem(item);
	}

	public Map<Key, UserItem> updateItems(Map<Key, ChangeSet> changeSets) {
		List<Key> itemKeys = new ArrayList<Key>();
		for (Key itemKey : changeSets.keySet()) {
			ChangeSet change = changeSets.get(itemKey);
			if (change.isEmpty()) {
				// empty
				continue;
			}
			if (!change.getUserItem().equals(itemKey)) {
				// invalid
				continue;
			}
			itemKeys.add(itemKey);
		}

		ItemService is = new ItemService();
		User user = UserServiceFactory.getUserService().getCurrentUser();
		return is.updateItems(itemKeys, changeSets, user);
	}
}
