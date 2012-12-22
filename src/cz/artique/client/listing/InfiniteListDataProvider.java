package cz.artique.client.listing;

import java.util.List;

public interface InfiniteListDataProvider<V> {
	void onScrollEnd();

	int getHeadSize();

	void pushHead();

	List<V> getValues(boolean includeHead);

	boolean isEndReached();

	void changeValue(V value);
}
