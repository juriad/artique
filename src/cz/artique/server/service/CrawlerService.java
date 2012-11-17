package cz.artique.server.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slim3.datastore.Datastore;

import cz.artique.server.crawler.Crawler;
import cz.artique.server.crawler.CrawlerResult;
import cz.artique.server.crawler.HTMLCrawler;
import cz.artique.server.crawler.ManualCrawler;
import cz.artique.server.crawler.XMLCrawler;
import cz.artique.shared.model.source.HTMLSource;
import cz.artique.shared.model.source.ManualSource;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.Stats;
import cz.artique.shared.model.source.XMLSource;

public class CrawlerService {
	public CrawlerResult fetchItems(Source source) {
		Crawler<? extends Source> c = createCrawler(source);
		CrawlerResult cr = c.fetchItems();
		
		return cr;
	}

	private void addStats(Source source, CrawlerResult cr) {
		Stats s = new Stats();
		s.setProbeDate(new Date());
		s.setSource(source.getKey());
		s.setItemsAcquired(cr.getItemsCount());

		if (cr.isError()) {
			List<Throwable> ts = cr.getErrors();
			s.setError(ts.get(ts.size() - 1).getLocalizedMessage());
		}

		Datastore.put(s);
	}

	private Crawler<? extends Source> createCrawler(Source source) {
		if (source instanceof XMLSource) {
			return new XMLCrawler((XMLSource) source);
		} else if (source instanceof HTMLSource) {
			return new HTMLCrawler((HTMLSource) source);
		} else if (source instanceof ManualSource) {
			return new ManualCrawler((ManualSource) source);
		} else {
			return null;
		}
	}

	private void setNextCheck(Source source, CrawlerResult cr) {
		// TODO rozsirit
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, 10);
		Date next = calendar.getTime();
		source.setNextCheck(next);
	}

	private void setErrorSequence(Source source, CrawlerResult cr) {
		if (cr.isError()) {
			source.setErrorSequence(source.getErrorSequence() + 1);
		} else {
			source.setErrorSequence(0);
		}
	}

	public boolean crawl(Source source) {
		CrawlerResult cr = fetchItems(source);
		addStats(source, cr);
		setNextCheck(source, cr);
		setErrorSequence(source, cr);
		return !cr.isError();
	}
}
