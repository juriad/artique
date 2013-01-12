package cz.artique.client.listing;

import java.util.List;

import com.google.gwt.view.client.HasRows;

public interface InfiniteList<E> extends HasRows, HasScrollEndHandlers {

	void appendValues(List<E> values);

	void prependValues(List<E> values);

	void setValue(E value);

	int showHead();

	int showTail();

	void clear();

	void setRowCountExact(boolean rowCountExact);

	InfiniteListDataProvider<E> getProvider();

	void setProvider(InfiniteListDataProvider<E> provider);
}
