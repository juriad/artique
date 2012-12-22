package cz.artique.client.listing;

import java.util.List;

import com.google.gwt.view.client.HasRows;

public interface InfiniteList<V> extends HasRows {

	void appendValues(List<V> values);

	void prependValues(List<V> values);

	void setValue(V value);

	int showHead();

	int showTail();

	void clear();

	void refresh();
}
