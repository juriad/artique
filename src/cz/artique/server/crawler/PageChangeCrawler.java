package cz.artique.server.crawler;

import java.util.LinkedList;

import org.jsoup.select.Elements;

import com.google.appengine.api.datastore.Text;

import cz.artique.server.crawler.DiffMatchPatch.Diff;
import cz.artique.server.service.ConfigService;
import cz.artique.shared.model.config.ConfigKey;
import cz.artique.shared.model.item.ContentType;
import cz.artique.shared.model.item.PageChangeItem;
import cz.artique.shared.model.source.PageChangeSource;

public class PageChangeCrawler {

	private final PageChangeSource source;

	public PageChangeCrawler(PageChangeSource source) {
		this.source = source;
	}

	public PageChangeItem getPageChange(Elements doc) {
		String newContent = doc.text();
		String oldContent =
			getSource().getContent() == null ? null : getSource()
				.getContent()
				.getValue();
		if (oldContent == null) {
			oldContent = "";
		}

		String diff = generateDiff(oldContent, newContent);
		if (diff == null) {
			return null;
		}

		PageChangeItem change = getPageChangeItem(doc, diff);
		return change;
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

	public PageChangeSource getSource() {
		return source;
	}
}
