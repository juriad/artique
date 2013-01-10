package cz.artique.shared.utils;

import com.google.appengine.api.datastore.Key;

public interface GenKey extends HasKey<Key> {
	Key getKeyParent();

	String getKeyName();
}
