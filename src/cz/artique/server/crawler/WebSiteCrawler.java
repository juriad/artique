package cz.artique.server.crawler;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.Text;

import cz.artique.server.service.ItemService;
import cz.artique.server.utils.ServerUtils;
import cz.artique.shared.model.item.ContentType;
import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.item.LinkItem;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.source.Region;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.UserSource;
import cz.artique.shared.model.source.WebSiteSource;

/**
 * Crawls {@link WebSiteCrawler} and adds new {@link LinkItem}s to system for
 * all users watching crawled {@link Source}.
 * 
 * @author Adam Juraszek
 * 
 */
public class WebSiteCrawler extends HTMLCrawler<WebSiteSource, LinkItem> {

	/**
	 * Constructs crawler for {@link WebSiteSource}.
	 * 
	 * @param source
	 *            source
	 */
	public WebSiteCrawler(WebSiteSource source) {
		super(source);
	}

	/**
	 * @param page
	 *            elements matching region
	 * @return list of {@link LinkItem}s
	 */
	private Map<String, LinkItem> getLinks(Elements page) {
		Map<String, LinkItem> urls = new HashMap<String, LinkItem>();
		Elements linkElements = page.select("a");

		for (Element link : linkElements) {
			String linkHref = link.attr("href");
			if (linkHref.isEmpty()) {
				continue;
			}
			urls.put(linkHref, getItem(link));
		}
		return urls;
	}

	/**
	 * @param link
	 *            link element
	 * @return prototype of {@link LinkItem} for link
	 */
	private LinkItem getItem(Element link) {
		LinkItem item = new LinkItem(getSource());
		String linkHref = link.attr("href");
		String linkText = link.text();

		item.setContent(new Text(link.parent().text()));
		item.setContentType(ContentType.PLAIN_TEXT);
		item.setTitle(linkText);
		item.setUrl(new Link(linkHref));
		item.setHash(getHash(item));
		return item;
	}

	/**
	 * @param item
	 *            item the hash is calculated for
	 * @return hash for item
	 */
	protected String getHash(Item item) {
		String url = item.getUrl().getValue();
		return ServerUtils.toSHA1(getSource().getUrl().getValue() + "|" + url);
	}

	/**
	 * Downloads the page specified by {@link Source#getUrl()}, builds DOM tree,
	 * filters the page by {@link Region} and finds all anchors. For each anchor
	 * a new {@link LinkItem} is created and added to system if it doesn't
	 * exist.
	 * 
	 * @see cz.artique.server.crawler.Crawler#fetchItems()
	 */
	public int fetchItems() throws CrawlerException {
		URI uri;
		try {
			uri = getURI(getSource().getUrl());
		} catch (CrawlerException e) {
			writeStat(e);
			throw e;
		}

		Document doc;
		try {
			doc = getDocument(uri);
		} catch (CrawlerException e) {
			writeStat(e);
			throw e;
		}

		List<UserSource> userSources = getUserSources();

		Map<String, LinkItem> forItems = new HashMap<String, LinkItem>();
		Map<String, List<UserSource>> forSources =
			new HashMap<String, List<UserSource>>();
		for (UserSource us : userSources) {
			if (us.getRegionObject() == null) {
				continue;
			}
			Document doc2 = doc.clone();
			Elements filteredPage = filterPage(us.getRegionObject(), doc2);
			Map<String, LinkItem> links = getLinks(filteredPage);
			forItems.putAll(links);
			for (String link : links.keySet()) {
				if (!forSources.containsKey(link)) {
					forSources.put(link, new ArrayList<UserSource>());
				}
				forSources.get(link).add(us);
			}
		}

		int count = 0;
		for (String link : forItems.keySet()) {
			LinkItem item = forItems.get(link);
			List<UserSource> sources = forSources.get(link);
			LinkItem duplicate = createNonDuplicateItem(item);
			if (duplicate != null) {
				item = duplicate;
			}

			Set<String> usersAlreadyHavingItem =
				getUsersAlreadyHavingItem(item);
			List<UserItem> userItems = new ArrayList<UserItem>();
			for (UserSource us : sources) {
				if (usersAlreadyHavingItem.contains(us.getUserId())) {
					continue;
				}
				UserItem userItem = createUserItem(us, item);
				userItems.add(userItem);
			}
			if (userItems.size() > 0) {
				saveUserItems(userItems);
				count++;
			}
		}

		return count;
	}

	@Override
	protected List<LinkItem> getCollidingItems(LinkItem item) {
		ItemService is = new ItemService();
		List<LinkItem> collidingLinkItems =
			is.getCollidingLinkItems(getSource().getKey(), item.getHash());
		return collidingLinkItems;
	}
}
