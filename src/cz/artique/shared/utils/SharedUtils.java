package cz.artique.shared.utils;

import java.util.Collection;
import java.util.Iterator;

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

	public static <E> boolean eq(E e1, E e2) {
		if (e1 == null) {
			return e1 == e2;
		} else {
			return e1.equals(e2);
		}
	}

	public static <E extends HasDeepEquals<E>> boolean deepEq(E e1, E e2) {
		if (e1 == null) {
			return e1 == e2;
		} else {
			return e1.equalsDeeply(e2);
		}
	}

	public static <E extends HasDeepEquals<E>> boolean deepEq(
			Collection<E> es1, Collection<E> es2) {
		if (es1 == null) {
			return es1 == es2;
		} else {
			if (es1.size() != es2.size()) {
				return false;
			} else {
				for (Iterator<E> i1 = es1.iterator(), i2 = es2.iterator(); i1
					.hasNext() && i2.hasNext();) {
					if (!deepEq(i1.next(), i2.next())) {
						return false;
					}
				}
				return true;
			}
		}
	}
}
