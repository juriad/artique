package cz.artique.client;

import com.google.gwt.i18n.client.ConstantsWithLookup;

public interface ArtiqueConstants extends ConstantsWithLookup {
	@DefaultStringValue("General")
	String options();

	@DefaultStringValue("Sources")
	String sources();

	@DefaultStringValue("Filters")
	String filters();

	@DefaultStringValue("History")
	String history();

	@DefaultStringValue("Messages")
	String messages();

	@DefaultStringValue("Application is unavailable. Some managers failed to load.")
	String failedToLoadManagers();

	@DefaultStringValue("Application has been successfuly initialized.")
	String initialized();

	@DefaultStringValue("Connection to server is unavailable.")
	String offline();

	@DefaultStringValue("Connection to server has been restored.")
	String gotOnline();

	@DefaultStringValue("Connection to server has been lost.")
	String gotOffline();

	@DefaultStringValue("Logout")
	String logout();

	@DefaultStringValue("Edit labels")
	String editLabelsButton();

	@DefaultStringValue("Edit shortcuts")
	String editShortcutsButton();

	@DefaultStringValue("Mark read above selected")
	String markAllReadButton();

	@DefaultStringValue("Refresh")
	String refreshButton();

	@DefaultStringValue("Add new items")
	String addNewItems();

	@DefaultStringValue("No new items")
	String noNewItems();
}