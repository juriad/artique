package cz.artique.client.listFilters;

import com.google.gwt.i18n.client.ConstantsWithLookup;

public interface ListFiltersConstants extends ConstantsWithLookup {

	@DefaultStringValue("Filter editor")
	String listFilterEditor();

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

	@DefaultStringValue("Read")
	String readState_READ();

	@DefaultStringValue("Unread")
	String readState_UNREAD();
	
	@DefaultStringValue("Shortcut")
	String shortcut();
}