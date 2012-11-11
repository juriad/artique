package cz.artique.server.service;

import java.util.List;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.users.User;

import cz.artique.server.meta.item.ItemMeta;
import cz.artique.shared.model.item.Item;

public class ItemService {
	public List<Item> getItems(User user) {

		ItemMeta meta = ItemMeta.get();
		List<Item> items = Datastore.query(meta).sort(meta.added.desc).asList();
		return items;
	}
}
