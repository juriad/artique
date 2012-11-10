package cz.artique.server.crawler;

import java.net.URL;

import cz.artique.shared.model.source.HTMLSource;

public class HTMLCrawler extends AbstractCrawler<HTMLSource> {

	public HTMLCrawler(HTMLSource source) {
		super(source);
	}

	public CrawlerResult fetchItems() {
		CrawlerResult result = new CrawlerResult();

		URL url = getURL(result);
		if (url == null) {
			return result;
		}

		return result;
	}

}
