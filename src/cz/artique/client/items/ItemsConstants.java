package cz.artique.client.items;

import com.google.gwt.i18n.client.ConstantsWithLookup;

public interface ItemsConstants extends ConstantsWithLookup {

	@DefaultStringValue("Insert a new item manually")
	String manualItemDialog();

	@DefaultStringValue("URL")
	String url();

	@DefaultStringValue("Title")
	String title();

	@DefaultStringValue("Content")
	String content();

	@DefaultStringValue("Labels")
	String labels();

	@DefaultStringValue("Save")
	String saveButton();

	@DefaultStringValue("Cancel")
	String cancelButton();
}
