package cz.artique.server.crawler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import com.google.appengine.api.datastore.Text;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;

import cz.artique.shared.model.item.ArticleItem;
import cz.artique.shared.model.item.ContentType;
import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.source.XMLSource;

public class XMLCrawler extends AbstractCrawler<XMLSource> {

	public XMLCrawler(XMLSource source) {
		super(source);
	}

	public CrawlerResult fetchItems() {
		CrawlerResult result = new CrawlerResult();

		URI uri = getURI(result);
		if (uri == null) {
			return result;
		}

		SyndFeed feed = getFeed(result, uri);
		if (feed == null) {
			return result;
		}

		getItems(result, feed);
		addItems(result);
		return result;
	}

	protected void addItems(CrawlerResult result) {
		CrawlerResult res = new CrawlerResult();
		res.getErrors().addAll(result.getErrors());
		for (Item item : result.getItems()) {
			boolean added = putItemIfNotDuplicate(source, item);
			if (added) {
				res.addItem(item);
			}
		}
		result = res;
	}

	protected SyndFeed getFeed(CrawlerResult result, URI uri) {
		SyndFeedInput input = new SyndFeedInput();
		SyndFeed feed = null;
		try {
			HttpClient client = getHttpClient();
			HttpGet get = new HttpGet(uri);
			HttpResponse resp = client.execute(get);
			InputStream is = resp.getEntity().getContent();
			feed = input.build(new InputStreamReader(is));
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
			ArticleItem a = new ArticleItem(source);

			a.setTitle(entry.getTitle());
			a.setContent(new Text(entry.getDescription().getValue()));
			a.setContentType(ContentType
				.guess(entry.getDescription().getType()));
			a.setAuthor(entry.getAuthor());
			a.setPublished(entry.getPublishedDate());

			try {
				a.setHash(getHash(entry));
			} catch (CrawlerException e) {
				result.addError(e);
			}
			result.addItem(a);
		}
	}

	protected String getHash(SyndEntry entry) {
		String id = entry.getUri();
		if (id == null) {
			id = entry.getTitle();
		}
		return CrawlerUtils.toSHA1(source.getUrl().getValue() + "|" + id);
	}

}
