package cz.artique.server.service;

import java.util.Date;
import java.util.List;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import cz.artique.client.service.ClientItemService;
import cz.artique.server.crawler.CrawlerUtils;
import cz.artique.shared.model.item.ManualItem;
import cz.artique.shared.model.item.UserItem;

public class ClientItemServiceImpl implements ClientItemService {

	public List<UserItem> getItems() {
		ItemService is = new ItemService();
		User user = UserServiceFactory.getUserService().getCurrentUser();
		return is.getItems(user);
	}

	public UserItem addItem(ManualItem item) {
		if (item.getTitle() == null || item.getTitle().isEmpty()) {
			// TODO throw empty title
		}
		if (item.getUrl() == null || item.getUrl().getValue().isEmpty()) {
			// TODO throw empty url
		}

		ItemService is = new ItemService();
		item.setAdded(new Date());
		item.setHash(genManualItemHash(item));
		return is.addManualItem(item);
	}

	private String genManualItemHash(ManualItem item) {
		return CrawlerUtils.toSHA1(item.getUrl().getValue() + "|"
			+ item.getTitle());
	}
}
