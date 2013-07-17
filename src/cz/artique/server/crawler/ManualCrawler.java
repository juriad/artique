package cz.artique.server.crawler;

import java.util.ArrayList;
import java.util.List;

import cz.artique.shared.model.item.ManualItem;
import cz.artique.shared.model.source.ManualSource;

/**
 * Uninteresting crawler for {@link ManualSource} - such crawler does not do
 * anything.
 * 
 * @author Adam Juraszek
 * 
 */
public class ManualCrawler extends AbstractCrawler<ManualSource, ManualItem> {

	/**
	 * Constructs crawler for {@link ManualSource}.
	 * 
	 * @param source
	 *            source
	 */
	public ManualCrawler(ManualSource source) {
		super(source);
	}

	/**
	 * Does not do anything useful; just returns new {@link ArrayList}
	 * 
	 * @see cz.artique.server.crawler.AbstractCrawler#getCollidingItems(cz.artique.shared.model.item.Item)
	 */
	@Override
	protected List<ManualItem> getCollidingItems(ManualItem item) {
		return new ArrayList<ManualItem>();
	}

	/**
	 * Does not do anything useful;just returns zero.
	 * 
	 * @see cz.artique.server.crawler.Crawler#fetchItems()
	 */
	public int fetchItems() throws CrawlerException {
		return 0;
	}

}
