package cz.artique.server.service;

import java.util.Date;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import cz.artique.client.service.ClientItemService;
import cz.artique.shared.list.ListingUpdate;
import cz.artique.shared.list.ListingUpdateRequest;
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

	public UserItem updateUserItem(UserItem item)
			throws NullPointerException, SecurityBreachException {
		if (item == null) {
			throw new NullPointerException();
		}
		Sanitizer.checkUser("user", item.getUser());

		ItemService is = new ItemService();
		is.updateUserItem(item);
		return item;
	}
}
