package cz.artique.shared.utils;


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
}
