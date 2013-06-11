package cz.artique.shared.utils;

import java.util.Collection;
import java.util.Iterator;

/**
 * Contains bunch of common functions used in model = shared code.
 * 
 * @author Adam Juraszek
 * 
 */
public class SharedUtils {
	private SharedUtils() {}

	/**
	 * Combines all tokens into one string joining them by dollar sign.
	 * 
	 * @param strings
	 *            vararg list of tokens
	 * @return one string which contains all tokens separated by dollar sign
	 */
	public static String combineStringParts(String... strings) {
		if (strings == null || strings.length == 0) {
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

	/**
	 * Tests two objects of the same type for equality by
	 * {@link Object#equals(Object)} method.
	 * Both of them may be null.
	 * 
	 * @param e1
	 *            the first object
	 * @param e2
	 *            the other object
	 * @return true if they are equal, false otherwise
	 */
	public static <E> boolean eq(E e1, E e2) {
		if (e1 == null) {
			return e1 == e2;
		} else {
			return e1.equals(e2);
		}
	}

	/**
	 * Tests two objects of the same type implementing {@link HasDeepEquals} for
	 * equality by {@link HasDeepEquals#equalsDeeply(Object)} method.
	 * Both of them may be null.
	 * 
	 * @param e1
	 *            the first object
	 * @param e2
	 *            the other object
	 * @return true if they are equal, false otherwise
	 */
	public static <E extends HasDeepEquals<E>> boolean deepEq(E e1, E e2) {
		if (e1 == null) {
			return e1 == e2;
		} else {
			return e1.equalsDeeply(e2);
		}
	}

	/**
	 * Tests two collections of objects of the same type implementing
	 * {@link HasDeepEquals} for equality one-by-one by
	 * {@link HasDeepEquals#equalsDeeply(Object)} method.
	 * Both of them may be null.
	 * 
	 * @param e1
	 *            the first collection
	 * @param e2
	 *            the other collection
	 * @return true if they are equal, false otherwise
	 */
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
