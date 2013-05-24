package cz.artique.server.utils;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

import cz.artique.shared.utils.GenKey;

public class ServerUtils {

	private ServerUtils() {}

	public static Key genKey(GenKey model) {
		String name = model.getKeyName();
		Class<? extends GenKey> clazz = model.getClass();
		return Datastore.createKey(clazz, name);
	}
}
