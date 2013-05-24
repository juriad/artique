package cz.artique.server.crawler;

import java.util.ArrayList;
import java.util.List;

import cz.artique.shared.model.item.ManualItem;
import cz.artique.shared.model.source.ManualSource;

public class ManualCrawler extends AbstractCrawler<ManualSource, ManualItem> {

	public ManualCrawler(ManualSource source) {
		super(source);
	}

	@Override
	protected List<ManualItem> getCollidingItems(ManualItem item) {
		return new ArrayList<ManualItem>();
	}

	public int fetchItems() throws CrawlerException {
		return 0;
	}

}
