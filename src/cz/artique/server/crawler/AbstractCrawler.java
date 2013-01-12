package cz.artique.server.crawler;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

import cz.artique.server.meta.item.ItemMeta;
import cz.artique.server.meta.source.UserSourceMeta;
import cz.artique.server.utils.GAEConnectionManager;
import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.UserSource;

public abstract class AbstractCrawler<E extends Source> implements Crawler<E> {

	protected final E source;

	protected AbstractCrawler(E source) {
		this.source = source;
	}

	protected URI getURI(CrawlerResult result) {
		URI uri = null;
		try {
			uri = new URI(source.getUrl().getValue());

		} catch (URISyntaxException e) {
			result.addError(new CrawlerException("Wrong URI syntax", e));
		}
		return uri;
	}

	protected HttpClient getHttpClient() {
		HttpParams httpParams = new BasicHttpParams();
		ClientConnectionManager connectionManager = new GAEConnectionManager();
		HttpClient httpClient =
			new DefaultHttpClient(connectionManager, httpParams);
		return httpClient;
	}

	protected boolean putItemIfNotDuplicate(Source source, Item item) {
		ItemMeta meta = ItemMeta.get();
		List<Item> items =
			Datastore
				.query(meta)
				.filter(meta.source.equal(source.getKey()))
				.filter(meta.hash.equal(item.getHash()))
				.asList();

		for (Item i : items) {
			if (isDuplicate(item, i)) {
				return false;
			}
		}
		Key key = Datastore.put(item);
		item.setKey(key);

		createUserItems(source, item);
		return true;
	}

	protected void createUserItems(Source source, Item item) {
		UserSourceMeta meta = UserSourceMeta.get();
		List<UserSource> userSources =
			Datastore
				.query(meta)
				.filter(meta.source.equal(source.getKey()))
				.filter(meta.watching.equal(Boolean.TRUE))
				.asList();
		List<UserItem> userItems = new ArrayList<UserItem>();
		for (UserSource us : userSources) {
			UserItem ui = createUserItem(us, item);
			userItems.add(ui);
		}
		Datastore.put(userItems);
	}

	protected UserItem createUserItem(UserSource us, Item item) {
		UserItem ui = new UserItem();
		ui.setAdded(item.getAdded());
		ui.setItem(item.getKey());
		ui.setRead(false);
		ui.setUser(us.getUser());
		ui.setUserSource(us.getKey());
		ui.setLabels(us.getDefaultLabels());
		return ui;
	}

	protected boolean isDuplicate(Item i1, Item i2) {
		if (i1.getSource().equals(i2.getSource())) {
			// same sources
			if (i1.getHash().equals(i2.getHash())) {
				// same hashes
				if ((i1.getUrl() != null && i2.getUrl() != null) ? i1
					.getUrl()
					.equals(i2.getUrl()) : i1.getUrl() == i2.getUrl()) {
					return true;
				}
			}
		}
		return false;
	}
}
