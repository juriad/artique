package cz.artique.server.crawler;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

import cz.artique.server.crawler.DiffMatchPatch.Diff;
import cz.artique.server.meta.source.PageChangeCrawlerDataMeta;
import cz.artique.server.service.ConfigService;
import cz.artique.shared.model.config.ConfigKey;
import cz.artique.shared.model.item.ContentType;
import cz.artique.shared.model.item.PageChangeItem;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.source.PageChangeCrawlerData;
import cz.artique.shared.model.source.PageChangeSource;
import cz.artique.shared.model.source.Region;
import cz.artique.shared.model.source.UserSource;

public class PageChangeCrawler
		extends HTMLCrawler<PageChangeSource, PageChangeItem> {

	public PageChangeCrawler(PageChangeSource source) {
		super(source);
	}

	public int fetchItems() throws CrawlerException {
		URI uri;
		try {
			uri = getURI(getSource().getUrl());
		} catch (CrawlerException e) {
			writeStat(e);
			throw e;
		}

		Document doc;
		try {
			doc = getDocument(uri);
		} catch (CrawlerException e) {
			writeStat(e);
			throw e;
		}

		List<UserSource> userSources = getUserSources();
		Map<Region, List<UserSource>> byRegion =
			new HashMap<Region, List<UserSource>>();
		for (UserSource userSource : userSources) {
			if (userSource.getRegionObject() != null) {
				Region region = userSource.getRegionObject();
				if (!byRegion.containsKey(region)) {
					byRegion.put(region, new ArrayList<UserSource>());
				}
				byRegion.get(region).add(userSource);
			}
		}

		int count = 0;
		for (Region region : byRegion.keySet()) {
			Document doc2 = doc.clone();
			Elements filteredPage = filterPage(region, doc2);
			handleByRegion(region, filteredPage, byRegion.get(region));
		}

		return count;
	}

	private void handleByRegion(Region region, Elements filteredPage,
			List<UserSource> list) {
		List<UserSource> firstTime = new ArrayList<UserSource>();
		List<UserSource> older = new ArrayList<UserSource>();
		PageChangeCrawlerData data = null;
		for (UserSource us : list) {
			if (us.getCrawlerData() == null) {
				firstTime.add(us);
			} else {
				if (data == null) {
					data =
						Datastore.get(PageChangeCrawlerDataMeta.get(),
							us.getCrawlerData());
				}
				older.add(us);
			}
		}

		String newContent = filteredPage.text();
		if (data != null) {
			String oldContent = data.getContent().getValue();
			data.setContent(new Text(newContent));
			Datastore.put(data);

			String diff = generateDiff(oldContent, newContent);
			if (diff != null) {
				PageChangeItem item = getPageChangeItem(filteredPage, diff);
				List<UserItem> userItems = new ArrayList<UserItem>();
				for (UserSource us : older) {
					UserItem userItem = createUserItem(us, item);
					userItems.add(userItem);
				}
				Datastore.put(userItems);
			}
		} else {
			if (firstTime.size() > 0) {
				data = new PageChangeCrawlerData();
				data.setContent(new Text(newContent));
				Key key = Datastore.put(data);
				data.setKey(key);
			}
		}

		if (firstTime.size() > 0) {
			for (UserSource us : firstTime) {
				us.setCrawlerData(data.getKey());
			}
			Datastore.put(firstTime);
		}
	}

	protected PageChangeItem getPageChangeItem(Elements page, String diff) {
		PageChangeItem change = new PageChangeItem(getSource());
		change.setComparedTo(getSource().getLastCheck());
		change.setContent(new Text(page.outerHtml()));
		change.setContentType(ContentType.HTML);
		change.setDiff(new Text(diff));
		change.setDiffType(ContentType.HTML);

		change.setTitle("Change of page " + getSource().getUrl().getValue());
		change.setUrl(getSource().getUrl());
		change.setHash(getHash(change));
		return change;
	}

	protected String getHash(PageChangeItem item) {
		String content = item.getContent().getValue();
		return CrawlerUtils.toSHA1(getSource().getUrl().getValue() + "|"
			+ content);
	}

	protected String generateDiff(String oldContent, String newContent) {
		DiffMatchPatch dmp = new DiffMatchPatch();
		dmp.Diff_EditCost =
			ConfigService.CONFIG_SERVICE
				.getConfig(ConfigKey.DIFF_EDIT_COST)
				.<Long> get()
				.shortValue();
		dmp.Diff_Timeout =
			ConfigService.CONFIG_SERVICE
				.getConfig(ConfigKey.DIFF_TIMEOUT)
				.<Double> get()
				.floatValue();

		LinkedList<Diff> diffs = dmp.diff_main(oldContent, newContent);
		if (diffs.size() == 0) {
			// texts are identical
			return null;
		}

		dmp.diff_cleanupEfficiency(diffs);
		String prettyHtml = dmp.diff_prettyHtml(diffs);
		return prettyHtml;
	}

	@Override
	protected List<PageChangeItem> getCollidingItems(PageChangeItem item) {
		return new ArrayList<PageChangeItem>();
	}
}
