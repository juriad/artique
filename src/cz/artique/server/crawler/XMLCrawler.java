package cz.artique.server.crawler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Text;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;

import cz.artique.server.meta.item.ArticleItemMeta;
import cz.artique.shared.model.item.ArticleItem;
import cz.artique.shared.model.item.ContentType;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.source.UserSource;
import cz.artique.shared.model.source.XMLSource;

public class XMLCrawler extends AbstractCrawler<XMLSource, ArticleItem> {

	public XMLCrawler(XMLSource source) {
		super(source);
	}

	public int fetchItems() throws CrawlerException {
		URI uri;
		try {
			uri = getURI(getSource().getUrl());
		} catch (CrawlerException e) {
			writeStat(e);
			throw e;
		}

		SyndFeed feed;
		try {
			feed = getFeed(uri);
		} catch (CrawlerException e) {
			writeStat(e);
			throw e;
		}

		List<ArticleItem> items = getItems(feed);
		List<ArticleItem> items2 = createNonDuplicateItems(items);
		createUserItems(items2);

		int count = items2.size();
		writeStat(count);
		return count;
	}

	protected void createUserItems(List<ArticleItem> items) {
		List<UserSource> userSources = getUserSources();
		for (ArticleItem item : items) {
			List<UserItem> userItems = new ArrayList<UserItem>();
			for (UserSource us : userSources) {
				UserItem ui = createUserItem(us, item);
				userItems.add(ui);
			}
			Datastore.put(userItems);
		}
	}

	protected SyndFeed getFeed(URI uri) throws CrawlerException {
		SyndFeedInput input = new SyndFeedInput();
		try {
			HttpClient client = getHttpClient();
			HttpGet get = new HttpGet(uri);
			HttpResponse resp = client.execute(get);
			InputStream is = resp.getEntity().getContent();
			SyndFeed feed = input.build(new InputStreamReader(is));
			return feed;
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

	protected ArticleItem getItem(SyndEntry entry) {
		ArticleItem a = new ArticleItem(getSource());
		a.setTitle(entry.getTitle());
		a.setContent(new Text(entry.getDescription().getValue()));
		a.setContentType(ContentType.guess(entry.getDescription().getType()));
		a.setAuthor(entry.getAuthor());
		a.setPublished(entry.getPublishedDate());
		a.setHash(getHash(entry));
		return a;
	}

	protected String getHash(SyndEntry entry) {
		String id = entry.getUri();
		if (id == null) {
			id = entry.getTitle();
		}
		return CrawlerUtils.toSHA1(getSource().getUrl().getValue() + "|" + id);
	}

	// FIXME move datastore to service
	@Override
	protected List<ArticleItem> getCollidingItems(ArticleItem item) {
		ArticleItemMeta meta = ArticleItemMeta.get();
		List<ArticleItem> items =
			Datastore
				.query(meta)
				.filter(meta.source.equal(getSource().getKey()))
				.filter(meta.hash.equal(item.getHash()))
				.asList();
		return items;
	}

}
