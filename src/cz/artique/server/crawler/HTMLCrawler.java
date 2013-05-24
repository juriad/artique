package cz.artique.server.crawler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.users.User;

import cz.artique.server.meta.item.UserItemMeta;
import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.source.HTMLSource;
import cz.artique.shared.model.source.Region;

public abstract class HTMLCrawler<E extends HTMLSource, F extends Item>
		extends AbstractCrawler<E, F> {

	protected HTMLCrawler(E source) {
		super(source);
	}

	protected Document getDocument(URI uri) throws CrawlerException {
		HttpClient httpClient = getHttpClient();
		HttpGet get = new HttpGet(uri);

		HttpResponse resp;
		try {
			resp = httpClient.execute(get);
		} catch (IOException e) {
			throw new CrawlerException("Cannot execute request");
		}

		HttpEntity entity = resp.getEntity();
		InputStream is;
		try {
			is = entity.getContent();
		} catch (IOException e) {
			throw new CrawlerException("Cannot get response content");
		}

		String charset = EntityUtils.getContentCharSet(entity);
		if (charset == null) {
			charset = HTTP.DEFAULT_CONTENT_CHARSET;
		}

		Document document;
		try {
			document = Jsoup.parse(is, charset, uri.toString());
		} catch (IOException e) {
			throw new CrawlerException("Cannot parse html page");
		}
		return document;
	}

	protected Elements filterPage(Region region, Document doc) {
		String positive = region.getPositiveSelector();
		List<String> negatives = region.getNegativeSelectors();
		Elements positiveElements = doc.select(positive);
		if (positiveElements.isEmpty()) {
			return positiveElements;
		}

		for (Element e : positiveElements) {
			for (String negative : negatives) {
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
		UserItemMeta meta = UserItemMeta.get();
		Iterable<UserItem> asIterable =
			Datastore
				.query(meta)
				.filter(meta.item.equal(item.getKey()))
				.asIterable();
		Set<User> users = new HashSet<User>();
		for (UserItem ui : asIterable) {
			users.add(ui.getUser());
		}
		return users;
	}
}
