package cz.artique.client.listFilters;

import com.google.gwt.i18n.client.ConstantsWithLookup;

public interface ListFiltersConstants extends ConstantsWithLookup {

	@DefaultStringValue("Filter editor")
	String listFilterDialog();

	@DefaultStringValue("Delete")
	String deleteButton();

	@DefaultStringValue("Wipe")
	String wipeButton();

	@DefaultStringValue("Save")
	String saveButton();

	@DefaultStringValue("Cancel")
	String cancelButton();

	@DefaultStringValue("Apply")
	String applyButton();

	@DefaultStringValue("Close")
	String closeButton();

	@DefaultStringValue("Name")
	String name();

	@DefaultStringValue("Hierarchy")
	String hierarchy();

	@DefaultStringValue("Export alias")
	String exported();

	@DefaultStringValue("From")
	String startFrom();

	@DefaultStringValue("To")
	String endTo();

	@DefaultStringValue("Read")
	String readState();

	@DefaultStringValue("Order")
	String sortOrder();

	@DefaultStringValue("Descending")
	String listFilterOrder_DESCENDING();

	@DefaultStringValue("Ascending")
	String listFilterOrder_ASCENDING();

	@DefaultStringValue("All")
	String readState_ALL();

	@DefaultStringValue("Read")
	String readState_READ();

	@DefaultStringValue("Unread")
	String readState_UNREAD();

	@DefaultStringValue("Shortcut")
	String shortcut();

	@DefaultStringValue("Ad-hoc filter editor")
	String adhocDialog();

	@DefaultStringValue("^ Mark read ^")
	String markAllRead();

	@DefaultStringValue("Edit filter")
	String editFilter();

	@DefaultStringValue("Refresh")
	String refresh();

	@DefaultStringValue("Add new items")
	String addNewItems();

	@DefaultStringValue("No new items")
	String noNewItems();

	@DefaultStringValue("Clear labels")
	String clearLabels();

	@DefaultStringValue("Clear all")
	String clearAll();

	@DefaultStringValue("Current filter:")
	String currentFilter();

	@DefaultStringValue("items")
	String items();

	@DefaultStringValue("ordered")
	String ordered();
	
	@DefaultStringValue("No labels criteria")
	String noLabelsFilter();
}
