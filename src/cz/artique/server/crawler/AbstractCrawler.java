package cz.artique.server.crawler;

import java.net.MalformedURLException;
import java.net.URL;

import cz.artique.shared.model.source.Source;

public abstract class AbstractCrawler<E extends Source> implements Crawler<E> {

	protected final E source;
	
	protected AbstractCrawler(E source) {
		this.source = source;
	}

	protected URL getURL(CrawlerResult result) {
		URL url = null;
		try {
			url = new URL(source.getUrl().getValue());
		} catch (MalformedURLException e) {
			result.addError(new CrawlerException("Malformed url", e));
		}
		return url;
	}
}
