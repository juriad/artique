package cz.artique.shared.utils;

import com.google.appengine.api.datastore.Key;

public interface GenKey {
	Key getKeyParent();

	String getKeyName();
}
