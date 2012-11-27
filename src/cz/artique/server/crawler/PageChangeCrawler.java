package cz.artique.server.crawler;

import java.util.LinkedList;

import org.jsoup.select.Elements;

import com.google.appengine.api.datastore.Text;

import cz.artique.server.crawler.DiffMatchPatch.Diff;
import cz.artique.server.service.ConfigService;
import cz.artique.shared.model.item.ContentType;
import cz.artique.shared.model.item.PageChangeItem;
import cz.artique.shared.model.source.PageChangeSource;
import cz.artique.shared.model.user.ConfigOption;

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
		ConfigService cs = new ConfigService();
		dmp.Diff_EditCost =
			(short) cs.getLongValue(ConfigOption.DIFF_EDIT_COST);
		dmp.Diff_Timeout = (float) cs.getDoubleValue(ConfigOption.DIFF_TIMEOUT);

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
