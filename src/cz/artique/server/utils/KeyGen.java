package cz.artique.server.utils;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

import cz.artique.shared.utils.GenKey;

/**
 * Generator of keys for model classes which implement {@link GenKey}.
 * 
 * @author Adam Juraszek
 * 
 */
public class KeyGen {

	private KeyGen() {}

	/**
	 * Generates {@link Key} for model implementing {@link GenKey}.
	 * 
	 * @param model
	 *            model object
	 * @return generated key
	 */
	public static Key genKey(GenKey model) {
		String name = model.getKeyName();
		Class<? extends GenKey> clazz = model.getClass();
		return Datastore.createKey(clazz, name);
	}
}
