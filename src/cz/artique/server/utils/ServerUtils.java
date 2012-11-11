package cz.artique.server.utils;

import java.net.MalformedURLException;
import java.net.URL;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Link;

import cz.artique.shared.utils.GenKey;

public class ServerUtils {

	private ServerUtils() {}

	public static Link checkLink(Link url) {
		if (url == null) {
			return null;
		}
		try {
			new URL(url.getValue()); // test url
			return url;
		} catch (MalformedURLException e) {
			return null;
		}
	}

	public static Key genKey(GenKey model) {
		Key parent = model.getKeyParent();
		String name = model.getKeyName();
		Class<? extends GenKey> clazz = model.getClass();
		if (parent != null) {
			return Datastore.createKey(clazz, name);
		}
		return Datastore.createKey(parent, clazz, name);
	}
}
