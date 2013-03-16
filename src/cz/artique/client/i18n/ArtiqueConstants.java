package cz.artique.client.i18n;

import com.google.gwt.i18n.client.ConstantsWithLookup;

public interface ArtiqueConstants extends ConstantsWithLookup {
	@DefaultStringValue("No title provided")
	String missingTitle();

	@DefaultStringValue("No content provided.")
	String missingContent();

	@DefaultStringValue("Unknown source")
	String missingUserSource();

	@DefaultStringValue("Operator AND")
	String operatorAnd();

	@DefaultStringValue("Operator OR")
	String operatorOr();

	@DefaultStringValue("Sources")
	String sources();

	@DefaultStringValue("Filters")
	String filters();

	@DefaultStringValue("History")
	String history();

	@DefaultStringValue("Date from")
	String startFrom();

	@DefaultStringValue("Date to")
	String endTo();

	@DefaultStringValue("Read state")
	String readState();

	@DefaultStringValue("Order")
	String sortOrder();

	@DefaultStringValue("Ascending")
	String listFilterOrder_ASCENDING();

	@DefaultStringValue("Descending")
	String listFilterOrder_DESCENDING();

	@DefaultStringValue("Read")
	String readState_READ();

	@DefaultStringValue("Unread")
	String readState_UNREAD();
}
