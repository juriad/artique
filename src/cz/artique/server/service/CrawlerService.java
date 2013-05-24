package cz.artique.server.service;

import java.util.Calendar;
import java.util.Date;

import org.slim3.datastore.Datastore;

import cz.artique.server.crawler.Crawler;
import cz.artique.server.crawler.CrawlerException;
import cz.artique.server.crawler.ManualCrawler;
import cz.artique.server.crawler.PageChangeCrawler;
import cz.artique.server.crawler.WebSiteCrawler;
import cz.artique.server.crawler.XMLCrawler;
import cz.artique.shared.model.source.ManualSource;
import cz.artique.shared.model.source.PageChangeSource;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.WebSiteSource;
import cz.artique.shared.model.source.XMLSource;

public class CrawlerService {

	private Crawler<? extends Source> createCrawler(Source source) {
		if (source instanceof XMLSource) {
			return new XMLCrawler((XMLSource) source);
		} else if (source instanceof WebSiteSource) {
			return new WebSiteCrawler((WebSiteSource) source);
		} else if (source instanceof PageChangeSource) {
			return new PageChangeCrawler((PageChangeSource) source);
		} else if (source instanceof ManualSource) {
			return new ManualCrawler((ManualSource) source);
		} else {
			return null;
		}
	}

	private void setNextCheck(Source source, int count) {
		// TODO rozsirit
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, 10);
		Date next = calendar.getTime();
		source.setNextCheck(next);
	}

	public boolean crawl(Source source) {
		try {
			Crawler<? extends Source> crawler = createCrawler(source);
			int count = crawler.fetchItems();
			source.setErrorSequence(0);
			source.setLastCheck(new Date());
			setNextCheck(source, count);
			return true;
		} catch (CrawlerException e) {
			source.setErrorSequence(source.getErrorSequence() + 1);
			setNextCheck(source, -1);
			return false;
		} finally {
			Datastore.put(source);
		}
	}
}
