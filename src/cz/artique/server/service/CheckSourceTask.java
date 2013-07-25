package cz.artique.server.service;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.taskqueue.DeferredTask;

import cz.artique.shared.model.source.Source;

/**
 * Task which is added to Task Queue in order to delay {@link Source} check.
 * 
 * @author Adam Juraszek
 * 
 */
public class CheckSourceTask implements DeferredTask {

	private static final long serialVersionUID = 1L;

	private final Key sourceKey;

	/**
	 * Saves state-less key of {@link Source}.
	 * 
	 * @param sourceKey
	 *            key of source
	 */
	public CheckSourceTask(Key sourceKey) {
		this.sourceKey = sourceKey;
	}

	/**
	 * Gets {@link Source} from database and delegates it to
	 * {@link CrawlerService#crawl(Source)}.
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		SourceService ss = new SourceService();
		Source source = ss.getSourceByKey(sourceKey);
		if (!source.isEnabled()) {
			// it is nonsence to check this source
			return;
		}
		CrawlerService cs = new CrawlerService();
		boolean ok = cs.crawl(source);
		ss.setEnqued(source, false);

		if (!ok) {
			throw new RuntimeException("Crawling source "
				+ source.getUrl().getValue() + " failed.");
		}
	}
}
