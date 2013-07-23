package cz.artique.server.crawler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.select.Elements;

import com.google.appengine.api.datastore.Text;

import cz.artique.server.crawler.DiffMatchPatch.Diff;
import cz.artique.server.service.ConfigService;
import cz.artique.server.service.UserSourceService;
import cz.artique.server.utils.ServerTextContent;
import cz.artique.server.utils.ServerUtils;
import cz.artique.shared.model.config.server.ServerConfigKey;
import cz.artique.shared.model.item.ContentType;
import cz.artique.shared.model.item.PageChangeItem;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.source.PageChangeCrawlerData;
import cz.artique.shared.model.source.PageChangeSource;
import cz.artique.shared.model.source.Region;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.UserSource;

/**
 * Crawls {@link PageChangeSource} and adds new {@link PageChangeItem}s to
 * system for all users watching crawled {@link Source}.
 * 
 * @author Adam Juraszek
 * 
 */
public class PageChangeCrawler
		extends HTMLCrawler<PageChangeSource, PageChangeItem> {

	/**
	 * Constructs crawler for {@link PageChangeSource}.
	 * 
	 * @param source
	 *            source
	 */
	public PageChangeCrawler(PageChangeSource source) {
		super(source);
	}

	/**
	 * Compares content text with older version. If the contents are different,
	 * a new {@link PageChangeItem} will be added.
	 * 
	 * @param region
	 *            region the filtered page is restrained to
	 * @param filteredPage
	 *            restrained page by region
	 * @param list
	 *            list of user sources watching region
	 * @return number of imported items
	 */
	@Override
	protected int handleByRegion(Region region, Elements filteredPage,
			List<UserSource> list) {
		UserSourceService uss = new UserSourceService();

		List<UserSource> firstTime = new ArrayList<UserSource>();
		List<UserSource> older = new ArrayList<UserSource>();
		PageChangeCrawlerData data = null;
		for (UserSource us : list) {
			if (us.getCrawlerData() == null) {
				firstTime.add(us);
			} else {
				if (data == null) {
					data = uss.getPageChangeCrawlerData(us.getCrawlerData());
				}
				older.add(us);
			}
		}

		int added = 0;

		String newContent = ServerTextContent.asPlainText(filteredPage);
		if (data != null) {
			String oldContent = data.getContent().getValue();
			data.setContent(new Text(newContent));
			uss.saveCrawlerData(data);

			String diff = generateDiff(oldContent, newContent);
			if (diff != null) {
				PageChangeItem item = getPageChangeItem(filteredPage, diff);
				createNonDuplicateItem(item);
				List<UserItem> userItems = new ArrayList<UserItem>();
				for (UserSource us : older) {
					UserItem userItem = createUserItem(us, item);
					userItems.add(userItem);
				}
				saveUserItems(userItems);
				added = 1;
			}
		} else {
			if (firstTime.size() > 0) {
				data = new PageChangeCrawlerData();
				data.setContent(new Text(newContent));
				uss.saveCrawlerData(data);
			}
		}

		if (firstTime.size() > 0) {
			uss.setCrawlerData(firstTime, data.getKey());
		}

		return added;
	}

	/**
	 * Creates a new {@link PageChangeItem} from
	 * 
	 * @param page
	 * @param diff
	 * @return
	 */
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

	/**
	 * @param item
	 *            item which calculate hash for
	 * @return calculated hash
	 */
	protected String getHash(PageChangeItem item) {
		String content = item.getContent().getValue();
		return ServerUtils.toSHA1(getSource().getUrl().getValue() + "|"
			+ content);
	}

	/**
	 * Calculates difference between old and new content.
	 * 
	 * @param oldContent
	 *            old content
	 * @param newContent
	 *            new content
	 * @return difference HTML if they are different, null if identical
	 */
	protected String generateDiff(String oldContent, String newContent) {
		DiffMatchPatch dmp = new DiffMatchPatch();
		dmp.Diff_EditCost =
			ConfigService.CONFIG_SERVICE
				.getConfig(ServerConfigKey.DIFF_EDIT_COST)
				.<Integer> get()
				.shortValue();
		dmp.Diff_Timeout =
			ConfigService.CONFIG_SERVICE
				.getConfig(ServerConfigKey.DIFF_TIMEOUT)
				.<Double> get()
				.floatValue();

		LinkedList<Diff> diffs = dmp.diff_main(oldContent, newContent);
		boolean identical = true;
		for (Diff diff : diffs) {
			if (!DiffMatchPatch.Operation.EQUAL.equals(diff.operation)) {
				identical = false;
				break;
			}
		}
		if (identical) {
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
