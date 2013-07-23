package cz.artique.server.crawler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.Text;

import cz.artique.server.service.ItemService;
import cz.artique.server.utils.ServerTextContent;
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
	 * @param region
	 *            region the page is restrained to
	 * @return list of {@link LinkItem}s
	 */
	private Map<String, LinkItem> getLinks(Elements page, Region region) {
		Map<String, LinkItem> urls = new HashMap<String, LinkItem>();
		Elements linkElements = page.select("a");

		for (Element link : linkElements) {
			String linkHref = link.attr("abs:href");
			if (linkHref.isEmpty()) {
				continue;
			}
			if (!urls.containsKey(linkHref)) {
				urls.put(linkHref, getItem(link, region));
			}
		}
		return urls;
	}

	/**
	 * @param link
	 *            link element
	 * @param region
	 *            region the page is restrained to
	 * @return prototype of {@link LinkItem} for link
	 */
	private LinkItem getItem(Element link, Region region) {
		LinkItem item = new LinkItem(getSource());
		String linkHref = link.attr("abs:href");
		String linkText = ServerTextContent.getPlainText(link);

		setContent(item, link);
		item.setTitle(linkText);
		item.setUrl(new Link(linkHref));
		item.setHash(getHash(item, region));
		return item;
	}

	private void setContent(LinkItem item, Element link) {
		// TODO nice to have: get content of link item based on
		item
			.setContent(new Text(ServerTextContent.getPlainText(link.parent())));
		item.setContentType(ContentType.PLAIN_TEXT);
	}

	/**
	 * @param item
	 *            item the hash is calculated for
	 * @param region
	 *            region the page is restrained to
	 * @return hash for item
	 */
	protected String getHash(Item item, Region region) {
		String url = item.getUrl().getValue();
		return ServerUtils.toSHA1(getSource().getUrl().getValue() + "|"
			+ KeyFactory.keyToString(region.getKey()) + "|" + url);
	}

	/**
	 * Finds all anchors. For each anchor a new {@link LinkItem} is created and
	 * added to system if it doesn't exist.
	 * 
	 * @param region
	 *            region the filtered page is restrained to
	 * @param filteredPage
	 *            restrained page by region
	 * @param list
	 *            list of user sources watching region
	 * @return number of imported items
	 */
	@Override
	protected int handleByRegion(Region region, Elements filteredPage,
			List<UserSource> list) {
		int count = 0;
		Map<String, LinkItem> links = getLinks(filteredPage, region);
		for (String link : links.keySet()) {
			LinkItem item = links.get(link);
			LinkItem duplicate = createNonDuplicateItem(item);
			if (duplicate != null) {
				continue;
			}

			List<UserItem> userItems = new ArrayList<UserItem>();
			for (UserSource us : list) {
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
