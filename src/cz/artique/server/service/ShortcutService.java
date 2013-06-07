package cz.artique.server.service;

import java.util.List;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

import cz.artique.server.meta.shortcut.ShortcutMeta;
import cz.artique.server.utils.KeyGen;
import cz.artique.shared.model.shortcut.Shortcut;

public class ShortcutService {
	public List<Shortcut> getAllShortcuts(String userId) {
		ShortcutMeta meta = ShortcutMeta.get();
		List<Shortcut> listOfShortcuts =
			Datastore.query(meta).filter(meta.userId.equal(userId)).asList();
		return listOfShortcuts;
	}

	public void createShortcut(Shortcut shortcut) {
		Key key = KeyGen.genKey(shortcut);
		shortcut.setKey(key);
		Datastore.put(shortcut);
	}

	public Shortcut getShortcutByKey(Key shortcutKey) {
		return Datastore.getOrNull(ShortcutMeta.get(), shortcutKey);
	}

	public void deleteShortcut(Key shortcutKey) {
		Datastore.delete(shortcutKey);
	}
}
