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
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

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
	public BlobKey backup(UserItem userItem, BackupLevel backupLevel)
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

		if (backupLevel.isInlineCss()) {
			embedAllStyleSheetLinks(doc);
		}
		absolutizeAllLinks(doc);

		try {
			return saveToBlobStore(doc);
		} catch (Exception e) {
			throw new CrawlerException("Failed to save html page to blob store");
		}
	}

	/**
	 * Saves downloaded altered webpage to blobstore
	 * 
	 * @param doc
	 *            DOM representation of webpage
	 * @return key of backup
	 * @throws IOException
	 *             when something went wrong during saving to blobstore
	 */
	private BlobKey saveToBlobStore(Document doc) throws IOException {
		String html = doc.outerHtml();

		FileService fileService = FileServiceFactory.getFileService();
		AppEngineFile file = fileService.createNewBlobFile("text/html");
		FileWriteChannel writeChannel =
			fileService.openWriteChannel(file, true);

		PrintWriter out =
			new PrintWriter(Channels.newWriter(writeChannel, "UTF8"));
		out.println(html);
		out.close();

		writeChannel.closeFinally();
		BlobKey blobKey = fileService.getBlobKey(file);
		return blobKey;
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

	public void serveBackup(String blobKey, HttpServletResponse response)
			throws IOException {
		BlobstoreService blobstoreService =
			BlobstoreServiceFactory.getBlobstoreService();
		BlobKey bk = new BlobKey(blobKey);
		blobstoreService.serve(bk, response);
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
