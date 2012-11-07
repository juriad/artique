package cz.artique.server.crawler;

import cz.artique.shared.model.source.HTMLSource;

public class HTMLCrawler implements Crawler<HTMLSource> {

	private final HTMLSource source;

	public HTMLCrawler(HTMLSource source) {
		this.source = source;
	}

	public CrawlerResult fetchItems() {
		// TODO Auto-generated method stub
		return null;
	}

}
