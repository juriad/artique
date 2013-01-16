package cz.artique.shared.utils;

import com.google.appengine.api.datastore.Key;

public class SharedUtils {
	private SharedUtils() {}

	public static String combineStringParts(String... strings) {
		if (strings.length == 0) {
			throw new IllegalArgumentException(
				"there must be at least one part");
		} else if (strings.length == 1) {
			return strings[0];
		}
		StringBuilder s = new StringBuilder(strings[0]);
		for (int i = 1; i < strings.length; i++) {
			s.append("$");
			s.append(strings[i]);
		}
		return s.toString();
	}

	public static Key min(Key... ks) {
		if (ks == null || ks.length == 0) {
			return null;
		}

		Key min = ks[0];
		for (int i = 1; i < ks.length; i++) {
			if (ks[i].compareTo(min) < 0) {
				min = ks[i];
			}
		}
		return min;
	}

	public static Key max(Key... ks) {
		if (ks == null || ks.length == 0) {
			return null;
		}

		Key max = ks[0];
		for (int i = 1; i < ks.length; i++) {
			if (ks[i].compareTo(max) > 0) {
				max = ks[i];
			}
		}
		return max;
	}
}
