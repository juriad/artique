package cz.artique.server.crawler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.Text;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;

import cz.artique.shared.model.item.ArticleItem;
import cz.artique.shared.model.item.ContentType;
import cz.artique.shared.model.source.XMLSource;

public class XMLCrawler implements Crawler<XMLSource> {

	private final XMLSource source;

	public XMLCrawler(XMLSource source) {
		this.source = source;
	}

	public CrawlerResult fetchItems() {
		CrawlerResult result = new CrawlerResult();

		URL url = getURL(result);
		if (url == null) {
			return result;
		}

		SyndFeed feed = getFeed(result, url);
		if (feed == null) {
			return result;
		}

		getItems(result, feed);
		return result;
	}

	protected URL getURL(CrawlerResult result) {
		URL url = null;
		try {
			url = new URL(source.getUrl().getValue());
		} catch (MalformedURLException e) {
			result.addError(new CrawlerException("Malformed url", e));
		}
		return url;
	}

	protected SyndFeed getFeed(CrawlerResult result, URL url) {
		SyndFeedInput input = new SyndFeedInput();
		SyndFeed feed = null;
		try {
			feed = input.build(new InputStreamReader(url.openStream()));
		} catch (IllegalArgumentException e) {
			result.addError(new CrawlerException("Unsupported feed", e));
		} catch (FeedException e) {
			result.addError(new CrawlerException("Cannot parse feed", e));
		} catch (IOException e) {
			result.addError(new CrawlerException("Cannot open connection", e));
		}
		return feed;
	}

	protected void getItems(CrawlerResult result, SyndFeed feed) {

		@SuppressWarnings("unchecked")
		List<SyndEntry> entries = feed.getEntries();
		for (SyndEntry entry : entries) {
			ArticleItem a = new ArticleItem();

			a.setAdded(new Date());
			a.setTitle(entry.getTitle());
			a.setContent(new Text(entry.getDescription().getValue()));
			a.setContentType(ContentType
				.guess(entry.getDescription().getType()));
			a.setAuthor(entry.getAuthor());
			a.setPublished(entry.getPublishedDate());

			a.setSource(source.getKey());
			try {
				a.setHash(getHash(entry));
			} catch (CrawlerException e) {
				result.addError(e);
			}
			result.addItem(a);
		}
	}

	private String getHash(SyndEntry entry) {
		String id = entry.getUri();
		if (id == null) {
			id = entry.getTitle();
		}
		return CrawlerUtils.toSHA1(source.getUrl().getValue() + "|" + id);
	}

}
