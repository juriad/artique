package cz.artique.client.i18n;

import com.google.gwt.i18n.client.Constants;

public interface ArtiqueConstants extends Constants {
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
}
