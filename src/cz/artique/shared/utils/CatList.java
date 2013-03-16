package cz.artique.shared.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class CatList<E> implements List<E> {

	List<List<E>> lists = new ArrayList<List<E>>();

	public CatList(List<E>... ls) {
		if (ls != null && ls.length > 0) {
			lists = Arrays.asList(ls);
		}
	}

	public int size() {
		int s = 0;
		for (List<E> l : lists) {
			s += l.size();
		}
		return s;
	}

	public boolean isEmpty() {
		for (List<E> l : lists) {
			if (!l.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public boolean contains(Object o) {
		for (List<E> l : lists) {
			if (l.contains(o)) {
				return true;
			}
		}

		return false;
	}

	public Iterator<E> iterator() {
		return listIterator();
	}

	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException();
	}

	public boolean add(E e) {
		throw new UnsupportedOperationException();
	}

	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	public boolean containsAll(Collection<?> c) {
		for (Object o : c) {
			if (!contains(o)) {
				return false;
			}
		}
		return true;
	}

	public boolean addAll(Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}

	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	public void clear() {
		throw new UnsupportedOperationException();
	}

	public E get(int index) {
		for (List<E> l : lists) {
			if (index < l.size()) {
				return l.get(index);
			}
			index -= l.size();
		}
		throw new IndexOutOfBoundsException();
	}

	public E set(int index, E element) {
		throw new UnsupportedOperationException();
	}

	public void add(int index, E element) {
		throw new UnsupportedOperationException();
	}

	public E remove(int index) {
		throw new UnsupportedOperationException();
	}

	public int indexOf(Object o) {
		int sum = 0;
		for (List<E> l : lists) {
			int i = l.indexOf(o);
			if (i >= 0) {
				return sum + i;
			}
			sum += l.size();
		}
		return -1;
	}

	public int lastIndexOf(Object o) {
		int sum = 0;
		for (List<E> l : lists) {
			int i = l.indexOf(o);
			if (i >= 0) {
				return sum + i;
			}
			sum += l.size();
		}
		return -1;
	}

	public ListIterator<E> listIterator() {
		return listIterator(0);
	}

	private int[] getListIndex(int index) {
		int listIndex = 0;
		for (List<E> l : lists) {
			if (index < l.size()) {
				return new int[] { listIndex, index };
			}
			index -= l.size();
			listIndex++;
		}
		throw new IndexOutOfBoundsException();
	}

	public ListIterator<E> listIterator(final int i) {
		int[] ii = getListIndex(i);
		final int startList = ii[0];
		final int startIndex = ii[1];

		return new ListIterator<E>() {

			ListIterator<List<E>> outer = lists.listIterator(startList);
			ListIterator<E> inner = outer.next().listIterator(startIndex);

			public boolean hasNext() {
				if (inner.hasNext()) {
					return true;
				} else if (outer.hasNext()) {
					inner = outer.next().listIterator();
					return hasNext();
				}
				return false;
			}

			public E next() {
				if (inner.hasNext()) {
					return inner.next();
				} else if (outer.hasNext()) {
					inner = outer.next().listIterator();
					return next();
				}
				throw new NoSuchElementException();
			}

			public boolean hasPrevious() {
				if (inner.hasPrevious()) {
					return true;
				} else if (outer.hasPrevious()) {
					inner = outer.previous().listIterator();
					return hasPrevious();
				}
				return false;
			}

			public E previous() {
				if (inner.hasPrevious()) {
					return inner.previous();
				} else if (outer.hasPrevious()) {
					inner = outer.previous().listIterator();
					return previous();
				}
				throw new NoSuchElementException();
			}

			public int nextIndex() {
				throw new UnsupportedOperationException();
			}

			public int previousIndex() {
				throw new UnsupportedOperationException();
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}

			public void set(E e) {
				throw new UnsupportedOperationException();
			}

			public void add(E e) {
				throw new UnsupportedOperationException();
			}
		};
	}

	public List<E> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException();
	}

}
