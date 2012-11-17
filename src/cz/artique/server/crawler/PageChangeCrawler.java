package cz.artique.server.crawler;

import java.util.LinkedList;

import org.jsoup.select.Elements;

import com.google.appengine.api.datastore.Text;

import cz.artique.server.crawler.DiffMatchPatch.Diff;
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
		String oldContent = getSource().getContent().getValue();
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
		// fool eclipse: dead code
		if (Boolean.TRUE.equals(true)) { // TODO user preference
			change.setContent(new Text(page.text()));
			change.setContentType(ContentType.PLAIN_TEXT);
		} else {
			change.setContent(new Text(page.outerHtml()));
			change.setContentType(ContentType.HTML);
		}
		change.setDiff(new Text(diff));
		change.setDiffType(ContentType.HTML);

		// TODO user preference
		change.setTitle("Change of page");
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
		// TODO constants
		dmp.Diff_EditCost = 10;
		dmp.Diff_Timeout = 0.1f;
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
