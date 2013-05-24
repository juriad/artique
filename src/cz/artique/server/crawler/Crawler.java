package cz.artique.server.crawler;

import cz.artique.shared.model.source.Source;

public interface Crawler<E extends Source> {
	int fetchItems() throws CrawlerException;
}
