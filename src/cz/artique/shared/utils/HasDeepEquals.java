package cz.artique.shared.utils;

/**
 * Duplicates {@link Class#equals(Object)} method for model classes. Method
 * {@link Class#equals(Object)} for model is defined as equality of keys.
 * That is not sufficient for determining change of object properties.
 * 
 * @author Adam Juraszek
 * 
 * @param <E>
 *            any type
 */
public interface HasDeepEquals<E> {
	/**
	 * Tests if this and the other object are equal ignoring datastore keys and
	 * comparing by logical key
	 * 
	 * @param e
	 *            the object
	 * @return true if e equals this object
	 */
	boolean equalsDeeply(E e);
}
