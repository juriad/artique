package cz.artique.server.crawler;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.source.HTMLSource;
import cz.artique.shared.model.source.Region;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.UserSource;

/**
 * Abstract base for crawlers of {@link HTMLSource}s. Contains methods which are
 * used by all subclasses.
 * 
 * @author Adam Juraszek
 * 
 * @param <E>
 *            type of {@link HTMLSource}
 * @param <F>
 *            type of items the E produces
 */
public abstract class HTMLCrawler<E extends HTMLSource, F extends Item>
		extends AbstractCrawler<E, F> {

	protected HTMLCrawler(E source) {
		super(source);
	}

	/**
	 * Downloads the page specified by {@link Source#getUrl()}, builds DOM tree,
	 * filters the page by {@link Region}s and calls
	 * {@link #handleByRegion(Region, Elements, List)}
	 * 
	 * @see cz.artique.server.crawler.Crawler#fetchItems()
	 */
	public int fetchItems() throws CrawlerException {
		URL url = getURL(getSource().getUrl());
		Document doc = getDocument(url);

		List<UserSource> userSources = getUserSources();
		Map<Region, List<UserSource>> byRegion =
			new HashMap<Region, List<UserSource>>();
		for (UserSource userSource : userSources) {
			if (userSource.getRegionObject() != null) {
				Region region = userSource.getRegionObject();
				if (!byRegion.containsKey(region)) {
					byRegion.put(region, new ArrayList<UserSource>());
				}
				byRegion.get(region).add(userSource);
			}
		}

		int count = 0;
		for (Region region : byRegion.keySet()) {
			Document doc2 = doc.clone();
			Elements filteredPage = filterPage(region, doc2);
			int added =
				handleByRegion(region, filteredPage, byRegion.get(region));
			count += added;
		}

		return count;
	}

	/**
	 * @param region
	 *            {@link Region}
	 * @param filteredPage
	 *            filtered page by {@link Region}
	 * @param list
	 *            list of {@link UserSource} interested in this {@link Region}
	 * @return number of created items
	 */
	protected abstract int handleByRegion(Region region, Elements filteredPage,
			List<UserSource> list);

	/**
	 * Filters document doc by region and returns only matched elements.
	 * 
	 * @param region
	 *            filter to be performed on document
	 * @param doc
	 *            document to filter
	 * @return matched elements
	 */
	private Elements filterPage(Region region, Document doc) {
		String positive = region.getPositiveSelector();
		String negative = region.getNegativeSelector();
		Elements positiveElements;
		if (positive == null || positive.trim().isEmpty()) {
			positiveElements = new Elements(doc);
		} else {
			positiveElements = doc.select(positive);
		}
		if (positiveElements.isEmpty()) {
			return positiveElements;
		}

		if (negative != null && !negative.trim().isEmpty()) {
			for (Element e : positiveElements) {
				Elements toRemove = e.select(negative);
				toRemove.remove();
			}
		}

		Elements simplified = new Elements();
		outer: for (Element e : positiveElements) {
			for (Element parent : e.parents()) {
				if (positiveElements.contains(parent)) {
					continue outer;
				}
			}
			simplified.add(e);
		}

		return simplified;
	}
}
