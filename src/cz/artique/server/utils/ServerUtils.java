package cz.artique.server.utils;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

import cz.artique.shared.utils.GenKey;

public class ServerUtils {

	private ServerUtils() {}

	public static Key genKey(GenKey model) {
		Key parent = model.getKeyParent();
		String name = model.getKeyName();
		Class<? extends GenKey> clazz = model.getClass();
		if (parent != null) {
			return Datastore.createKey(parent, clazz, name);
		}
		return Datastore.createKey(clazz, name);
	}
}
