package cz.artique.client.listing;

import com.google.gwt.i18n.client.ConstantsWithLookup;

public interface ListingConstants extends ConstantsWithLookup {
	@DefaultStringValue("Added")
	String added();

	@DefaultStringValue("Published")
	String published();

	@DefaultStringValue("Content is missing.")
	String missingContent();

	@DefaultStringValue("Author")
	String author();

	@DefaultStringValue("Compared to")
	String comparedTo();

	@DefaultStringValue("Manual source")
	String manualSourceTitle();

	@DefaultStringValue("No")
	String content_NO();

	@DefaultStringValue("HTML")
	String content_HTML();

	@DefaultStringValue("TXT")
	String content_TXT();

	@DefaultStringValue("HTML Diff")
	String content_DIFF_HTML();

	@DefaultStringValue("TXT Diff")
	String content_DIFF_TXT();

	@DefaultStringValue("Today")
	String today();

	@DefaultStringValue("Yesterday")
	String yesterday();
}
