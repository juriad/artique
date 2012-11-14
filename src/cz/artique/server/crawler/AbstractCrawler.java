package cz.artique.server.crawler;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import cz.artique.server.utils.GAEConnectionManager;
import cz.artique.shared.model.source.Source;

public abstract class AbstractCrawler<E extends Source> implements Crawler<E> {

	protected final E source;

	protected AbstractCrawler(E source) {
		this.source = source;
	}

	protected URI getURI(CrawlerResult result) {
		URI uri = null;
		try {
			uri = new URI(source.getUrl().getValue());

		} catch (URISyntaxException e) {
			result.addError(new CrawlerException("Wrong URI syntax", e));
		}
		return uri;
	}

	protected HttpClient getHttpClient() {
		HttpParams httpParams = new BasicHttpParams();
		ClientConnectionManager connectionManager = new GAEConnectionManager();
		HttpClient httpClient =
			new DefaultHttpClient(connectionManager, httpParams);
		return httpClient;
	}
}
