package cz.artique.server.crawler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.Text;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;

import cz.artique.server.service.ItemService;
import cz.artique.server.utils.ServerUtils;
import cz.artique.shared.model.item.ArticleItem;
import cz.artique.shared.model.item.ContentType;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.UserSource;
import cz.artique.shared.model.source.XMLSource;

/**
 * Crawls {@link XMLSource} and adds new {@link ArticleItem}s to system for all
 * users watching crawled {@link Source}.
 * 
 * @author Adam Juraszek
 * 
 */
public class XMLCrawler extends AbstractCrawler<XMLSource, ArticleItem> {

	/**
	 * Constructs crawler for {@link XMLSource}.
	 * 
	 * @param source
	 *            source
	 */
	public XMLCrawler(XMLSource source) {
		super(source);
	}

	/**
	 * 
	 * Creates a feed for URL specified by {@link Source#getUrl()}, gets all
	 * items and adds them to system as {@link ArticleItem}s if they doesn't
	 * exist yes.
	 * 
	 * @see cz.artique.server.crawler.Crawler#fetchItems()
	 */
	public int fetchItems() throws CrawlerException {
		URL url = getURL(getSource().getUrl());
		SyndFeed feed = getFeed(url);

		List<ArticleItem> items = getItems(feed);
		Collections.sort(items, new Comparator<ArticleItem>() {
			public int compare(ArticleItem o1, ArticleItem o2) {
				Date p1 = o1.getPublished();
				Date p2 = o2.getPublished();
				if (p1 != null && p2 != null) {
					return p1.compareTo(p2);
				}
				return 0;
			}
		});

		List<ArticleItem> items2 = createNonDuplicateItems(items);
		createUserItems(items2);

		int count = items2.size();
		return count;
	}

	/**
	 * Adds {@link UserItem}s for each {@link UserSource} of crawled
	 * {@link Source}.
	 * 
	 * @param items
	 *            item to add
	 */
	protected void createUserItems(List<ArticleItem> items) {
		List<UserSource> userSources = getUserSources();
		for (ArticleItem item : items) {
			List<UserItem> userItems = new ArrayList<UserItem>();
			for (UserSource us : userSources) {
				UserItem ui = createUserItem(us, item);
				userItems.add(ui);
			}
			saveUserItems(userItems);
		}
	}

	/**
	 * @param url
	 *            URL which the feed is to be got from
	 * @return feed
	 * @throws CrawlerException
	 */
	protected SyndFeed getFeed(URL url) throws CrawlerException {
		SyndFeedInput input = new SyndFeedInput();
		try {
			HttpEntity entity = getEntity(url.toURI());
			InputStream is = entity.getContent();
			String charSet = EntityUtils.getContentCharSet(entity);
			if (charSet == null) {
				charSet = "UTF-8";
			}
			SyndFeed feed = input.build(new InputStreamReader(is, charSet));
			return feed;
		} catch (URISyntaxException e) {
			throw new CrawlerException("URL is not valid URI", e);
		} catch (IllegalArgumentException e) {
			throw new CrawlerException("Unsupported feed", e);
		} catch (FeedException e) {
			throw new CrawlerException("Cannot parse feed", e);
		} catch (IOException e) {
			throw new CrawlerException("Cannot open connection", e);
		} catch (Exception e) {
			throw new CrawlerException("Unknown exception", e);
		}
	}

	/**
	 * @param feed
	 *            feed whcih the items are extracted from
	 * @return list of {@link ArticleItem}s for items in feed
	 */
	protected List<ArticleItem> getItems(SyndFeed feed) {
		List<ArticleItem> items = new ArrayList<ArticleItem>();
		@SuppressWarnings("unchecked")
		List<SyndEntry> entries = feed.getEntries();
		for (SyndEntry entry : entries) {
			ArticleItem item = getItem(entry);
			if (item != null) {
				items.add(item);
			}
		}
		return items;
	}

	/**
	 * Creates an {@link ArticleItem} for feed entry.
	 * 
	 * @param entry
	 *            feed entry
	 * @return item
	 */
	protected ArticleItem getItem(SyndEntry entry) {
		ArticleItem a = new ArticleItem(getSource());
		a.setTitle(entry.getTitle());

		SyndContent description = entry.getDescription();
		if (description != null) {
			a.setContent(new Text(description.getValue()));
			a.setContentType(ContentType.guess(description.getType(),
				description.getValue()));
		}
		a.setAuthor(entry.getAuthor());
		a.setPublished(entry.getPublishedDate());
		a.setHash(getHash(entry));
		a.setUrl(new Link(entry.getLink()));
		return a;
	}

	/**
	 * @param entry
	 *            item which the hash is calculated for
	 * @return calculated hash
	 */
	protected String getHash(SyndEntry entry) {
		String id = entry.getUri();
		if (id == null) {
			id = entry.getTitle();
		}
		return ServerUtils.toSHA1(getSource().getUrl().getValue() + "|" + id);
	}

	@Override
	protected List<ArticleItem> getCollidingItems(ArticleItem item) {
		ItemService is = new ItemService();
		List<ArticleItem> collidingArticleItems =
			is.getCollidingArticleItems(getSource().getKey(), item.getHash());
		return collidingArticleItems;
	}
}
