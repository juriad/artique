package cz.artique.server.crawler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cz.artique.server.service.ItemService;
import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.source.HTMLSource;
import cz.artique.shared.model.source.Region;

/**
 * Abstract base for crawlers of {@link HTMLSource}s. Contains methods which are
 * used by all subclasses.
 * 
 * @author Adam Juraszek
 * 
 * @param <E>
 * @param <F>
 */
public abstract class HTMLCrawler<E extends HTMLSource, F extends Item>
		extends AbstractCrawler<E, F> {

	protected HTMLCrawler(E source) {
		super(source);
	}

	/**
	 * Filters document doc by region and returns only matched elements.
	 * 
	 * @param region
	 *            filter to be performed on document
	 * @param doc
	 *            document to filter
	 * @return matched elements
	 */
	protected Elements filterPage(Region region, Document doc) {
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

	/**
	 * @param item
	 *            item
	 * @return list of users who already have the item
	 */
	protected Set<String> getUsersAlreadyHavingItem(F item) {
		ItemService is = new ItemService();
		List<UserItem> userItemsForItem = is.getUserItemsForItem(item.getKey());
		Set<String> users = new HashSet<String>();
		for (UserItem ui : userItemsForItem) {
			users.add(ui.getUserId());
		}
		return users;
	}
}
