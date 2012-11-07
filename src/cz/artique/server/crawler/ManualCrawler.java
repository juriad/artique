package cz.artique.server.crawler;

import cz.artique.shared.model.source.ManualSource;

public class ManualCrawler implements Crawler<ManualSource> {

	@SuppressWarnings("unused")
	private final ManualSource source;

	public ManualCrawler(ManualSource source) {
		this.source = source;
	}

	public CrawlerResult fetchItems() {
		return new CrawlerResult();
	}

}
