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
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;

import cz.artique.server.meta.item.LinkItemMeta;
import cz.artique.shared.model.item.ContentType;
import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.item.LinkItem;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.source.UserSource;
import cz.artique.shared.model.source.WebSiteSource;

public class WebSiteCrawler extends HTMLCrawler<WebSiteSource, LinkItem> {

	public WebSiteCrawler(WebSiteSource source) {
		super(source);
	}

	public Map<String, LinkItem> getLinks(Elements page) {
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

	private LinkItem getItem(Element link) {
		LinkItem item = new LinkItem(getSource());
		String linkHref = link.attr("href");
		String linkText = link.text();

		item.setContent(new Text(link.parent().outerHtml()));
		item.setContentType(ContentType.HTML);
		item.setTitle(linkText);
		item.setUrl(new Link(linkHref));
		item.setHash(getHash(item));
		return item;
	}

	protected String getHash(Item item) {
		String url = item.getUrl().getValue();
		return CrawlerUtils.toSHA1(getSource().getUrl().getValue() + "|" + url);
	}

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

			Set<User> usersAlreadyHavingItem = getUsersAlreadyHavingItem(item);
			List<UserItem> userItems = new ArrayList<UserItem>();
			for (UserSource us : sources) {
				if (usersAlreadyHavingItem.contains(us.getUser())) {
					continue;
				}
				UserItem userItem = createUserItem(us, item);
				userItems.add(userItem);
			}
			if (userItems.size() > 0) {
				Datastore.put(userItems);
				count++;
			}
		}

		return count;
	}

	@Override
	protected List<LinkItem> getCollidingItems(LinkItem item) {
		LinkItemMeta meta = LinkItemMeta.get();
		List<LinkItem> items =
			Datastore
				.query(meta)
				.filter(meta.source.equal(getSource().getKey()))
				.filter(meta.hash.equal(item.getHash()))
				.asList();
		return items;
	}
}
