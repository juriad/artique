package cz.artique.server.crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;

import com.google.appengine.api.datastore.Link;

import cz.artique.server.utils.GAEConnectionManager;

/**
 * Abstract base for classes which process web resource downloaded via HTTP.
 * 
 * @author Adam Juraszek
 * 
 */
public abstract class Fetcher {
	/**
	 * Converts Link to URI.
	 * 
	 * @param link
	 *            link
	 * @return URI
	 * @throws CrawlerException
	 */
	protected URL getURL(Link link) throws CrawlerException {
		if (link == null) {
			throw new NullPointerException();
		}
		try {
			return new URL(link.getValue());
		} catch (MalformedURLException e) {
			throw new CrawlerException("Wrong URL syntax", e);
		}
	}

	/**
	 * @return new HTTP client
	 */
	protected HttpClient getHttpClient() {
		HttpParams httpParams = new BasicHttpParams();
		ClientConnectionManager connectionManager = new GAEConnectionManager();
		HttpClient httpClient =
			new DefaultHttpClient(connectionManager, httpParams);
		return httpClient;
	}

	/**
	 * @param uri
	 *            URI of web resource to be downloaded
	 * @return entity from HTTP response
	 * @throws CrawlerException
	 */
	protected HttpEntity getEntity(URI uri) throws CrawlerException {
		HttpClient httpClient = getHttpClient();
		HttpGet get = new HttpGet(uri);

		HttpResponse resp;
		try {
			resp = httpClient.execute(get);
		} catch (IOException e) {
			throw new CrawlerException("Cannot execute request", e);
		} catch (Exception e) {
			throw new CrawlerException("Unknown exception", e);
		}

		HttpEntity entity = resp.getEntity();
		return entity;
	}

	/**
	 * Downloads web page via HTTP and builds DOM tree.
	 * 
	 * @param url
	 *            URL of web page to be downloaded
	 * @return DOM representation of web page with URL
	 * @throws CrawlerException
	 */
	protected Document getDocument(URL url) throws CrawlerException {
		Document document;
		try {
			Connection connection = HttpConnection.connect(url);
			document = connection.get();
		} catch (IOException e) {
			throw new CrawlerException("Cannot parse html page");
		}
		return document;
	}
}
