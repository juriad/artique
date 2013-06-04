package cz.artique.client.listing;

import com.google.gwt.i18n.client.ConstantsWithLookup;

public interface ListingConstants extends ConstantsWithLookup {
	@DefaultStringValue("Added")
	String added();
	
	@DefaultStringValue("Published")
	String published();

	@DefaultStringValue("Content is missing.")
	String missingContent();
}
