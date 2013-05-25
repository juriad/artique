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

	@DefaultStringValue("RSS or ATOM")
	String sourceType_RSS_ATOM();

	@DefaultStringValue("Page change")
	String sourceType_PAGE_CHANGE();

	@DefaultStringValue("Website")
	String sourceType_WEB_SITE();

	@DefaultStringValue("Manual")
	String sourceType_MANUAL();

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

	@DefaultStringValue("URL")
	String url();

	@DefaultStringValue("Watching")
	String watching();

	@DefaultStringValue("Auto-assigned labels")
	String defaultLabels();

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

	@DefaultStringValue("Last check")
	String lastCheck();

	@DefaultStringValue("Next check")
	String nextCheck();

	@DefaultStringValue("Check now")
	String checkNow();

	@DefaultStringValue("Source is beeing watched.")
	String watchingYes();

	@DefaultStringValue("Source is not beeing watched.")
	String watchingNo();

	@DefaultStringValue("Not available.")
	String unavailable();

	@DefaultStringValue("No check has been planned.")
	String noCheckPlanned();

	@DefaultStringValue("No check has been performed.")
	String notCheckedYet();

	@DefaultStringValue("Check error sequence")
	String errorSequence();

	@DefaultStringValue("There were no errors during last check.")
	String noErrorSequence();

	@DefaultStringValue("Source")
	String source();

	@DefaultStringValue("Region")
	String region();

	@DefaultStringValue("Source type")
	String sourceType();

	@DefaultStringValue("Source editor")
	String userSourceEditor();

	@DefaultStringValue("Watch")
	String watchButton();

	@DefaultStringValue("Unwatch")
	String unwatchButton();

	@DefaultStringValue("Set URL")
	String setUrl();

	@DefaultStringValue("Shows dialog with detail information about this source.")
	String detailUserSourceTooltip();

	@DefaultStringValue("Creates a new source.")
	String createNewUserSourceTooltip();

	@DefaultStringValue("Source has been verified.")
	String sourceCreated();

	@DefaultStringValue("Source didn't pass validation.")
	String sourceCreatedError();

	@DefaultStringValue("The source become active as soon as you select region.")
	String selectRegion();

	@DefaultStringValue("Positive selector")
	String positiveSelector();

	@DefaultStringValue("Negative selectors")
	String negativeSelectors();

	@DefaultStringValue("Check region")
	String checkRegion();

	@DefaultStringValue("Failed to load list of regions.")
	String failedToGetRegions();

	@DefaultStringValue("Region check failed.")
	String regionCheckError();

	@DefaultStringValue("Region passed the check.")
	String regionCheckPass();

	@DefaultStringValue("Custom")
	String customRegion();
}
