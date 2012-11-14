package cz.artique.server.crawler;

import cz.artique.shared.model.source.ManualSource;

public class ManualCrawler extends AbstractCrawler<ManualSource> {

	public ManualCrawler(ManualSource source) {
		super(source);
	}

	public CrawlerResult fetchItems() {
		return new CrawlerResult();
	}

}
