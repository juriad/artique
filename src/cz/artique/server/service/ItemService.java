package cz.artique.server.service;

import java.util.ArrayList;
import java.util.List;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

import cz.artique.server.meta.item.ItemMeta;
import cz.artique.server.meta.item.UserItemMeta;
import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.item.UserItem;

public class ItemService {
	public List<UserItem> getItems(User user) {

		UserItemMeta meta = UserItemMeta.get();
		List<UserItem> uItems =
			Datastore
				.query(meta)
				.filter(meta.user.equal(user))
				.sort(meta.added.desc)
				.asList();

		List<Key> itemKeys = new ArrayList<Key>(uItems.size());
		for (UserItem ui : uItems) {
			itemKeys.add(ui.getItem());
		}

		ItemMeta iMeta = ItemMeta.get();
		List<Item> items = Datastore.get(iMeta, itemKeys);
		for (int i = 0; i < items.size(); i++) {
			uItems.get(i).setFullItem(items.get(i));
		}

		return uItems;
	}
}
