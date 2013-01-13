package cz.artique.client.listing;

import java.util.Date;

public interface InfiniteListDataProvider<E> {

	boolean isEndReached();
	
	boolean fetch(int count);
	
	Date getLastFetch();
}
