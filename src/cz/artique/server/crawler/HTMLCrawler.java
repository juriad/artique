package cz.artique.server.crawler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.appengine.api.users.User;

import cz.artique.server.service.ItemService;
import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.source.HTMLSource;
import cz.artique.shared.model.source.Region;

public abstract class HTMLCrawler<E extends HTMLSource, F extends Item>
		extends AbstractCrawler<E, F> {

	protected HTMLCrawler(E source) {
		super(source);
	}

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

	protected Set<User> getUsersAlreadyHavingItem(F item) {
		ItemService is = new ItemService();
		List<UserItem> userItemsForItem = is.getUserItemsForItem(item.getKey());
		Set<User> users = new HashSet<User>();
		for (UserItem ui : userItemsForItem) {
			users.add(ui.getUser());
		}
		return users;
	}
}
