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

	@DefaultStringValue("Messages")
	String messages();

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

	@DefaultStringValue("Filter editor")
	String listFilterEditor();

	@DefaultStringValue("New")
	String newButton();

	@DefaultStringValue("Wipe")
	String wipeButton();

	@DefaultStringValue("Save")
	String saveButton();

	@DefaultStringValue("Delete")
	String deleteButton();

	@DefaultStringValue("Close")
	String closeButton();

	@DefaultStringValue("Cancel")
	String cancelButton();

	@DefaultStringValue("Name")
	String name();

	@DefaultStringValue("Hierarchy")
	String hierarchy();

	@DefaultStringValue("Export name")
	String exported();

	@DefaultStringValue("Adhoc")
	String adhoc();

	@DefaultStringValue("Clears current filter.")
	String clearListFilterTooltip();

	@DefaultStringValue("Creates a new filter.")
	String createNewListFilterTooltip();

	@DefaultStringValue("Saves current filter.")
	String saveCurrentListFilterTooltip();

	@DefaultStringValue("Shows dialog with detail information about this filter.")
	String detailListFilterTooltip();

	@DefaultStringValue("Click to adjust current filter.")
	String adhocTooltip();

	@DefaultStringValue("Apply")
	String applyButton();

	@DefaultStringValue("Clears whole history.")
	String clearHistoryTooltip();

	@DefaultStringValue("Clears all messages.")
	String clearMessagesTooltip();

}
