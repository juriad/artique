package cz.artique.server.crawler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.appengine.api.datastore.Link;

import cz.artique.server.utils.GAEConnectionManager;

public abstract class Fetcher {
	protected URI getURI(Link link) throws CrawlerException {
		if (link == null) {
			throw new NullPointerException();
		}
		try {
			return new URI(link.getValue());
		} catch (URISyntaxException e) {
			throw new CrawlerException("Wrong URI syntax", e);
		}
	}

	protected HttpClient getHttpClient() {
		HttpParams httpParams = new BasicHttpParams();
		ClientConnectionManager connectionManager = new GAEConnectionManager();
		HttpClient httpClient =
			new DefaultHttpClient(connectionManager, httpParams);
		return httpClient;
	}

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

	protected Document getDocument(URI uri) throws CrawlerException {
		HttpEntity entity = getEntity(uri);
		InputStream is;
		try {
			is = entity.getContent();
		} catch (IOException e) {
			throw new CrawlerException("Cannot get response content");
		}

		String charset = EntityUtils.getContentCharSet(entity);
		if (charset == null) {
			charset = HTTP.DEFAULT_CONTENT_CHARSET;
		}

		Document document;
		try {
			document = Jsoup.parse(is, charset, uri.toString());
		} catch (IOException e) {
			throw new CrawlerException("Cannot parse html page");
		}
		return document;
	}
}