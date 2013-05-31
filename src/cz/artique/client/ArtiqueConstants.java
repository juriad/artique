package cz.artique.client;

import com.google.gwt.i18n.client.ConstantsWithLookup;

public interface ArtiqueConstants extends ConstantsWithLookup {
	@DefaultStringValue("Sources")
	String sources();

	@DefaultStringValue("Filters")
	String filters();

	@DefaultStringValue("History")
	String history();

	@DefaultStringValue("Messages")
	String messages();
}
