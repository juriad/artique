package cz.artique.server.service;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.taskqueue.DeferredTask;

import cz.artique.shared.model.source.Source;

public class CheckSourceTask implements DeferredTask {

	private static final long serialVersionUID = 1L;

	private final Key sourceKey;

	public CheckSourceTask(Key sourceKey) {
		this.sourceKey = sourceKey;
	}

	public void run() {
		SourceService ss = new SourceService();
		Source source = ss.getSourceByKey(sourceKey);
		if (!source.isEnabled()) {
			// it is nonsence to check this source
			return;
		}
		CrawlerService cs = new CrawlerService();
		boolean ok = cs.crawl(source);
		try {
			if (ok) {
				source.setEnqued(false);
			} else {
				throw new RuntimeException("Crawling source "
					+ source.getUrl().getValue() + " failed.");
			}
		} finally {
			ss.saveSource(source);
		}
	}
}
