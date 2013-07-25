package cz.artique.server.service;

import java.util.List;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

import cz.artique.server.meta.shortcut.ShortcutMeta;
import cz.artique.server.utils.KeyGen;
import cz.artique.shared.model.shortcut.Shortcut;

/**
 * Provides methods which manipulates with entity {@link Shortcut} in
 * database.
 * 
 * @author Adam Juraszek
 * 
 */
public class ShortcutService {
	/**
	 * Gets list of all {@link Shortcut}s for a user.
	 * 
	 * @param userId
	 *            the user the {@link Shortcut}s are gotten for
	 * @return list of all {@link Shortcut}s
	 */
	public List<Shortcut> getAllShortcuts(String userId) {
		ShortcutMeta meta = ShortcutMeta.get();
		List<Shortcut> listOfShortcuts =
			Datastore.query(meta).filter(meta.userId.equal(userId)).asList();
		return listOfShortcuts;
	}

	/**
	 * Creates a new {@link Shortcut} in database.
	 * 
	 * @param shortcut
	 *            {@link Shortcut} to be created
	 */
	public void createShortcut(Shortcut shortcut) {
		Key key = KeyGen.genKey(shortcut);
		shortcut.setKey(key);
		Datastore.put(shortcut);
	}

	/**
	 * Gets {@link Shortcut} by its key.
	 * 
	 * @param shortcutKey
	 *            key of {@link Shortcut}
	 * @return {@link Shortcut}
	 */
	public Shortcut getShortcutByKey(Key shortcutKey) {
		return Datastore.getOrNull(ShortcutMeta.get(), shortcutKey);
	}

	/**
	 * Deletes {@link Shortcut} identified by its key.
	 * 
	 * @param shortcutKey
	 *            key of {@link Shortcut}
	 */
	public void deleteShortcut(Key shortcutKey) {
		Datastore.delete(shortcutKey);
	}
}
