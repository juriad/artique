package cz.artique.server.crawler;

public interface Crawler<E> {
	CrawlerResult fetchItems();
}
