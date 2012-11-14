package cz.artique.server.crawler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import cz.artique.server.service.SourceService;
import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.source.HTMLSource;
import cz.artique.shared.model.source.PageChangeSource;

public class HTMLCrawler extends AbstractCrawler<HTMLSource> {

	public HTMLCrawler(HTMLSource source) {
		super(source);
	}

	public CrawlerResult fetchItems() {
		CrawlerResult result = new CrawlerResult();

		URI uri = getURI(result);
		if (uri == null) {
			return result;
		}

		Document doc = getDocument(result, uri);
		if (doc == null) {
			return result;
		}
		
		SourceService ss = new SourceService();
		
		List<PageChangeSource> sources = ss.getPageChangeSources(source);
		for(PageChangeSource s: sources) {
			List<Item> items = processPageChange(s);
			// TODO pokracovat zde
		}

		return result;
	}

	private List<Item> processPageChange(PageChangeSource s) {
		// TODO Auto-generated method stub
		return null;
	}

	private Document getDocument(CrawlerResult result, URI uri) {
		try {
			HttpClient httpClient = getHttpClient();

			HttpGet get = new HttpGet(source.getUrl().getValue());
			HttpResponse resp = httpClient.execute(get);
			HttpEntity entity = resp.getEntity();

			InputStream is = entity.getContent();

			String charset = EntityUtils.getContentCharSet(entity);
			if (charset == null) {
				charset = HTTP.DEFAULT_CONTENT_CHARSET;
			}

			Document document = Jsoup.parse(is, charset, uri.toString());
			return document;
		} catch (IOException e) {
			result.addError(e);
			return null;
		}
	}

}
