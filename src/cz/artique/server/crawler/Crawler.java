package cz.artique.server.crawler;

import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.source.Source;

/**
 * Checks if {@link Source} has new items; if there are some, it will create
 * {@link Item} for each of them and one or more {@link UserItem}s for each user
 * watching that source.
 * 
 * @author Adam Juraszek
 * 
 * @param <E>
 *            source
 */
public interface Crawler<E extends Source> {
	/**
	 * Does the crawling and returns number of items acquired.
	 * 
	 * @return number of items acquired
	 * @throws CrawlerException
	 */
	int fetchItems() throws CrawlerException;
}
