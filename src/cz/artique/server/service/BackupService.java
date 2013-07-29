package cz.artique.server.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
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
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;

import cz.artique.server.crawler.CrawlerException;
import cz.artique.server.crawler.Fetcher;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.BackupLevel;
import cz.artique.shared.model.label.Label;

/**
 * Service which backs up webpages and serves the backups.
 * 
 * @author Adam Juraszek
 * 
 */
public class BackupService extends Fetcher {

	private static final String queueName = "backupItems";

	public BackupService() {}

	/**
	 * Does the backup.
	 * 
	 * @param userItem
	 *            {@link UserItem} to back up
	 * @param backupLevel
	 *            level of backup
	 * @return key of backup
	 * @throws CrawlerException
	 *             when something went wrong during backup
	 */
	public void backup(UserItem userItem, BackupLevel backupLevel)
			throws CrawlerException {
		if (userItem.getItemObject().getUrl() == null
			|| userItem.getItemObject().getUrl().getValue() == null) {
			throw new CrawlerException("Missing url.");
		}

		URL url;
		try {
			url = getURL(userItem.getItemObject().getUrl());
		} catch (CrawlerException e) {
			throw e;
		}

		Document doc;
		try {
			doc = getDocument(url);
		} catch (CrawlerException e) {
			throw e;
		}

		changeEncoding(doc);

		if (backupLevel.isInlineCss()) {
			embedAllStyleSheetLinks(doc);
		}
		absolutizeAllLinks(doc);

		try {
			saveToBlobStore(doc, userItem);
		} catch (Exception e) {
			throw new CrawlerException("Failed to save html page to blob store");
		}
	}

	/**
	 * Changes encoding of backed up page to utf-8.
	 * 
	 * @param doc
	 */
	private void changeEncoding(Document doc) {
		Elements elements = doc.select("meta[http-equiv=Content-Type]");
		elements.remove();
		elements = doc.select("meta[charset]");
		elements.remove();
		doc.head().appendElement("meta").attr("charset", "utf-8");
	}

	/**
	 * Saves downloaded altered webpage to blobstore
	 * 
	 * @param doc
	 *            DOM representation of webpage
	 * @param userItem
	 * @return key of backup
	 * @throws IOException
	 *             when something went wrong during saving to blobstore
	 */
	private void saveToBlobStore(Document doc, UserItem userItem)
			throws IOException {
		String html = doc.outerHtml();

		String name = KeyFactory.keyToString(userItem.getKey());
		GcsFilename filename = new GcsFilename("artique-data", name);
		GcsFileOptions options =
			new GcsFileOptions.Builder()
				.mimeType("text/html")
				.acl("public-read")
				.build();

		GcsService fileService = GcsServiceFactory.createGcsService();
		GcsOutputChannel writeChannel =
			fileService.createOrReplace(filename, options);

		PrintWriter out =
			new PrintWriter(Channels.newWriter(writeChannel, "UTF8"));
		out.println(html);
		out.close();
	}

	/**
	 * Makes all links (href, src, link) absolute.
	 * 
	 * @param doc
	 *            DOM representation of webpage
	 */
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

	/**
	 * Embeds external stylesheets into head as style elements.
	 * 
	 * @param doc
	 *            DOM representation of webpage
	 */
	private void embedAllStyleSheetLinks(Document doc) {
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
				link.after(style);
				link.remove();
			}
		}
	}

	/**
	 * @param url
	 *            URL of stylesheet
	 * @return downloaded stylesheet as string
	 */
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

	public void serveBackup(String userItemKey, HttpServletResponse response)
			throws IOException {
		GcsFilename filename = new GcsFilename("artique-data", userItemKey);
		BlobstoreService blobstoreService =
			BlobstoreServiceFactory.getBlobstoreService();
		BlobKey blobKey =
			blobstoreService.createGsBlobKey("/gs/" + filename.getBucketName()
				+ "/" + filename.getObjectName());
		blobstoreService.serve(blobKey, response);
	}

	/**
	 * Checks whether {@link UserItem} contains a {@link Label} which causes
	 * backup.
	 * 
	 * @param userItem
	 *            {@link UserItem} to be checked
	 */
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

	/**
	 * Creates {@link BackupTask} and enques it to Task Queue.
	 * 
	 * @param userItem
	 *            {@link UserItem} to be backed up
	 * @param label
	 *            {@link Label} which caused backing up
	 */
	public void doPlanForBackup(UserItem userItem, Label label) {
		Queue queue = QueueFactory.getQueue(queueName);
		TaskOptions task =
			TaskOptions.Builder.withPayload(new BackupTask(userItem.getKey(),
				label.getKey()));
		queue.add(task);
	}
}
