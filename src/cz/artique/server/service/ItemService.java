package cz.artique.server.service;

import java.util.ArrayList;
import java.util.List;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

import cz.artique.server.meta.item.ItemMeta;
import cz.artique.server.meta.item.UserItemMeta;
import cz.artique.server.utils.ServerUtils;
import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.item.ManualItem;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.source.UserSource;

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
			uItems.get(i).setItemObject(items.get(i));
		}

		return uItems;
	}

	public UserItem addManualItem(ManualItem item) {
		UserSourceService uss = new UserSourceService();
		UserSource manualSource = uss.getManualSource();

		item.setSource(manualSource.getSource());
		Key key = Datastore.put(item);
		item.setKey(key);

		UserItem ui = new UserItem(item, manualSource);
		ui.setKey(ServerUtils.genKey(ui));
		Datastore.put(ui);
		return ui;
	}

	public void updateUserItem(UserItem item) {
		Datastore.put(item);
	}
}
