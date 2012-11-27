package cz.artique.server.crawler;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.Text;

import cz.artique.shared.model.item.ContentType;
import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.item.LinkItem;
import cz.artique.shared.model.source.WebSiteSource;

public class WebSiteCrawler {
	private final WebSiteSource source;

	public WebSiteCrawler(WebSiteSource source) {
		this.source = source;
	}

	public List<Item> getLinks(Elements page) {
		Set<String> urls = new LinkedHashSet<String>();
		Elements linkElements = page.select("a");

		List<Item> items = new ArrayList<Item>();
		for (Element link : linkElements) {
			String linkHref = link.attr("href");
			boolean added = urls.add(linkHref);
			if (!added) {
				continue;
			}

			LinkItem item = getItem(link);
			items.add(item);
		}
		return items;
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

	public WebSiteSource getSource() {
		return source;
	}
}
