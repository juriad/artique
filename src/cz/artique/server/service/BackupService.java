package cz.artique.server.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.channels.Channels;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;

import cz.artique.server.crawler.CrawlerException;
import cz.artique.server.crawler.Fetcher;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.BackupLevel;
import cz.artique.shared.model.label.Label;

public class BackupService extends Fetcher {
	public BackupService() {}

	public BlobKey backup(UserItem userItem, BackupLevel backupLevel)
			throws CrawlerException {
		URI uri;
		try {
			uri = getURI(userItem.getItemObject().getUrl());
		} catch (CrawlerException e) {
			throw e;
		}

		Document doc;
		try {
			doc = getDocument(uri);
		} catch (CrawlerException e) {
			throw e;
		}

		if (backupLevel.isInlineCss()) {
			inlineAllStyleSheetLinks(doc);
		}
		absolutizeAllLinks(doc);

		try {
			return saveToBlobStore(doc);
		} catch (Exception e) {
			throw new CrawlerException("Failed to save html page to blob store");
		}
	}

	private BlobKey saveToBlobStore(Document doc) throws IOException {
		String html = doc.outerHtml();

		FileService fileService = FileServiceFactory.getFileService();
		AppEngineFile file = fileService.createNewBlobFile("text/html");
		FileWriteChannel writeChannel =
			fileService.openWriteChannel(file, false);

		// Different standard Java ways of writing to the channel
		// are possible. Here we use a PrintWriter:
		PrintWriter out =
			new PrintWriter(Channels.newWriter(writeChannel, "UTF8"));
		out.println(html);
		out.close();

		writeChannel.closeFinally();
		BlobKey blobKey = fileService.getBlobKey(file);
		return blobKey;
	}

	private void absolutizeAllLinks(Document doc) {
		Elements links = doc.select("a[href]");
		for (Element e : links) {
			e.attr("href", e.attr("abs:href"));
		}
		Elements media = doc.select("[src]");
		for (Element e : media) {
			e.attr("src", e.attr("abs:src"));
		}
		Elements imports = doc.select("link[href]");
		for (Element e : imports) {
			e.attr("href", e.attr("abs:href"));
		}
	}

	private void inlineAllStyleSheetLinks(Document doc) {
		final String linksSelector = "link[rel=stylesheet][href]";
		Elements links = doc.select(linksSelector);
		for (Element link : links) {
			String media = link.attr("media");
			Element style = new Element(Tag.valueOf("style"), doc.baseUri());
			if (!media.isEmpty()) {
				style.attr("media", media);
			}

			String url = link.attr("abs:href");
			String styleSheet = getStyleSheet(url);
			if (styleSheet != null) {
				style.text(styleSheet);
				link.remove();
			}
		}
	}

	private String getStyleSheet(String url) {
		try {
			HttpClient httpClient = getHttpClient();
			HttpGet get = new HttpGet(url);
			HttpResponse response = httpClient.execute(get);

			HttpEntity responseEntity = response.getEntity();
			if (responseEntity != null) {
				return EntityUtils.toString(responseEntity);
			}
		} catch (Exception e) {}
		return null;
	}

	public void serveBackup(String blobKey, HttpServletResponse response)
			throws IOException {
		// TODO call this
		BlobstoreService blobstoreService =
			BlobstoreServiceFactory.getBlobstoreService();
		BlobKey bk = new BlobKey(blobKey);
		blobstoreService.serve(bk, response);
	}

	public void planForBackup(UserItem userItem) {
		LabelService ls = new LabelService();
		List<Label> labelsByKeys = ls.getLabelsByKeys(userItem.getLabels());
		for (Label l : labelsByKeys) {
			if (l.getBackupLevel() != null
				&& !BackupLevel.NO_BACKUP.equals(l.getBackupLevel())) {
				doPlanForBackup(userItem, l);
				break;
			}
		}
	}

	private void doPlanForBackup(UserItem userItem, Label l) {
		// TODO Auto-generated method stub
		// synchronizace ve fronte
	}
}
