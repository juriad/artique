package cz.artique.server.crawler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

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

import cz.artique.server.service.SourceService;
import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.item.PageChangeItem;
import cz.artique.shared.model.source.HTMLSource;
import cz.artique.shared.model.source.PageChangeSource;
import cz.artique.shared.model.source.Region;
import cz.artique.shared.model.source.WebSiteSource;

public class HTMLCrawler extends AbstractCrawler<HTMLSource> {

	public HTMLCrawler(HTMLSource source) {
		super(source);
	}

	public CrawlerResult fetchItems() {
		CrawlerResult result = new CrawlerResult();

		URI uri = getURI(result);
		if (uri == null) {
			return result;
		}

		Document doc = getDocument(result, uri);
		if (doc == null) {
			return result;
		}

		SourceService ss = new SourceService();
		List<PageChangeSource> sources1 = ss.getPageChangeSources(source);
		for (PageChangeSource s : sources1) {
			Document doc2 = doc.clone();
			PageChangeCrawler crawler = new PageChangeCrawler(s);
			Elements filteredPage = filterPage(s.getRegionObject(), doc2);
			PageChangeItem pageChange = crawler.getPageChange(filteredPage);
			if (pageChange != null) {
				putItemIfNotDuplicate(s, pageChange);
			}
		}

		List<WebSiteSource> sources2 = ss.getWebSiteSources(source);
		for (WebSiteSource s : sources2) {
			Document doc2 = doc.clone();
			WebSiteCrawler crawler = new WebSiteCrawler(s);
			Elements filteredPage = filterPage(s.getRegionObject(), doc2);
			List<Item> links = crawler.getLinks(filteredPage);
			if (links != null && links.size() > 0) {
				for (Item link : links) {
					putItemIfNotDuplicate(s, link);
				}
			}
		}

		return result;
	}

	private Document getDocument(CrawlerResult result, URI uri) {
		try {
			HttpClient httpClient = getHttpClient();

			HttpGet get = new HttpGet(source.getUrl().getValue());
			HttpResponse resp = httpClient.execute(get);
			HttpEntity entity = resp.getEntity();

			InputStream is = entity.getContent();

			String charset = EntityUtils.getContentCharSet(entity);
			if (charset == null) {
				charset = HTTP.DEFAULT_CONTENT_CHARSET;
			}

			Document document = Jsoup.parse(is, charset, uri.toString());
			return document;
		} catch (IOException e) {
			result.addError(e);
			return null;
		}
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

}
