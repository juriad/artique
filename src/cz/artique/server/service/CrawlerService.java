package cz.artique.server.service;

import java.util.Date;

import cz.artique.server.crawler.Crawler;
import cz.artique.server.crawler.CrawlerException;
import cz.artique.server.crawler.ManualCrawler;
import cz.artique.server.crawler.PageChangeCrawler;
import cz.artique.server.crawler.WebSiteCrawler;
import cz.artique.server.crawler.XMLCrawler;
import cz.artique.shared.model.config.server.ServerConfigKey;
import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.source.CheckStat;
import cz.artique.shared.model.source.ManualSource;
import cz.artique.shared.model.source.PageChangeSource;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.WebSiteSource;
import cz.artique.shared.model.source.XMLSource;

/**
 * Initializes the crawling by calling proper {@link Crawler}.
 * 
 * @author Adam Juraszek
 * 
 */
public class CrawlerService {
	/**
	 * Creates {@link Crawler} for {@link Source} by its type.
	 * 
	 * @param source
	 *            {@link Source} the {@link Crawler} is to be created for
	 * @return {@link Crawler}
	 * @throws CrawlerException
	 *             when no such {@link Crawler} exists
	 */
	private Crawler<? extends Source> createCrawler(Source source)
			throws CrawlerException {
		if (source instanceof XMLSource) {
			return new XMLCrawler((XMLSource) source);
		} else if (source instanceof WebSiteSource) {
			return new WebSiteCrawler((WebSiteSource) source);
		} else if (source instanceof PageChangeSource) {
			return new PageChangeCrawler((PageChangeSource) source);
		} else if (source instanceof ManualSource) {
			return new ManualCrawler((ManualSource) source);
		} else {
			throw new CrawlerException("No such crawler type exists.");
		}
	}

	/**
	 * Heuristics which doubles interval when no {@link Item} was acquired and
	 * shrinks interval in case of many {@link Item}s.
	 * 
	 * @param count
	 *            number of items acquired during last check
	 * @return coefficient to be multiplied with length of last interval
	 */
	private double calcInterval(int count) {
		if (count == 0) {
			return 2.0;
		} else {
			return 1.0 / Math.sqrt(count);
		}
	}

	/**
	 * Sets nextCheck of source depending on number of acquired items.
	 * 
	 * @param source
	 *            {@link Source} the next check is set for
	 * @param count
	 *            number of items acquired during last check
	 */
	private void setNextCheck(Source source, int count) {
		// TODO nice to have: extend setNextCheck to be more intelligent

		Date nextCheck;
		if (count < 0) {
			int failedInterval =
				ConfigService.CONFIG_SERVICE
					.getConfig(ServerConfigKey.CRAWLER_CHECK_INTERVAL_FAILED)
					.<Integer> get();
			nextCheck =
				new Date(new Date().getTime() + failedInterval
					* source.getErrorSequence());
		} else if (source.getLastCheck() == null) {
			int firstInterval =
				ConfigService.CONFIG_SERVICE
					.getConfig(ServerConfigKey.CRAWLER_CHECK_INTERVAL_FIRST)
					.<Integer> get();
			nextCheck =
				new Date((long) (new Date().getTime() + firstInterval
					* calcInterval(count)));
		} else {
			int minInterval =
				ConfigService.CONFIG_SERVICE.getConfig(
					ServerConfigKey.CRAWLER_CHECK_INTERVAL_MIN).<Integer> get();
			int maxInterval =
				ConfigService.CONFIG_SERVICE.getConfig(
					ServerConfigKey.CRAWLER_CHECK_INTERVAL_MAX).<Integer> get();

			long lastInterval =
				source.getLastCheck().getTime() - new Date().getTime();
			int nextInterval =
				Math.min(Math.max((int) (lastInterval * calcInterval(count)),
					minInterval), maxInterval);
			nextCheck = new Date(new Date().getTime() + nextInterval);
		}
		source.setNextCheck(nextCheck);
	}

	/**
	 * Saves a new {@link CheckStat} describing check when it was successful.
	 * 
	 * @param items
	 *            number of items acquired
	 */
	protected void writeStat(Source s, int items) {
		CheckStat stat = new CheckStat();
		stat.setProbeDate(new Date());
		stat.setSource(s.getKey());
		stat.setItemsAcquired(items);
		SourceService ss = new SourceService();
		ss.addStat(stat);
	}

	/**
	 * Saves a new {@link CheckStat} describing this check when it failed.
	 * 
	 * @param t
	 *            exception which occurred while crawling
	 */
	protected void writeStat(Source s, Throwable t) {
		CheckStat stat = new CheckStat();
		stat.setProbeDate(new Date());
		stat.setSource(s.getKey());
		stat.setItemsAcquired(0);
		stat.setError(t.getLocalizedMessage());
		SourceService ss = new SourceService();
		ss.addStat(stat);
	}

	/**
	 * Initializes the crawling by calling proper {@link Crawler}.
	 * 
	 * @param source
	 *            {@link Source} to be crawled
	 * @return true for success; false for error
	 */
	public boolean crawl(Source source) {
		try {
			Crawler<? extends Source> crawler = createCrawler(source);
			int count = crawler.fetchItems();
			source.setErrorSequence(0);
			setNextCheck(source, count);
			source.setLastCheck(new Date());
			writeStat(source, count);
			return true;
		} catch (CrawlerException e) {
			source.setErrorSequence(source.getErrorSequence() + 1);
			setNextCheck(source, -1);
			writeStat(source, e);
			return false;
		}
	}
}
