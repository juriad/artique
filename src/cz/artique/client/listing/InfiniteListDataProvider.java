package cz.artique.client.listing;

import java.util.Date;

public interface InfiniteListDataProvider<E> {

	boolean isEndReached();
	
	void fetch(int count);
	
	Date getLastFetch();
}
