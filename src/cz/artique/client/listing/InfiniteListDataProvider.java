package cz.artique.client.listing;

public interface InfiniteListDataProvider<E> {
	int getHeadSize();

	void pushHead();

	boolean isEndReached();
}
