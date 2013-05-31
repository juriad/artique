package cz.artique.client.sources;

import com.google.gwt.i18n.client.ConstantsWithLookup;

public interface SourcesConstants extends ConstantsWithLookup {

	@DefaultStringValue("Source editor")
	String userSourceEditor();

	@DefaultStringValue("Save")
	String saveButton();

	@DefaultStringValue("Cancel")
	String cancelButton();

	@DefaultStringValue("Source type")
	String sourceType();

	@DefaultStringValue("URL")
	String url();

	@DefaultStringValue("Set URL")
	String setUrl();

	@DefaultStringValue("Name")
	String name();

	@DefaultStringValue("Watching")
	String watching();

	@DefaultStringValue("Hierarchy")
	String hierarchy();

	@DefaultStringValue("Default labels")
	String defaultLabels();

	@DefaultStringValue("Region")
	String region();

	@DefaultStringValue("Last check")
	String lastCheck();

	@DefaultStringValue("Error sequence")
	String errorSequence();

	@DefaultStringValue("Next check")
	String nextCheck();

	@DefaultStringValue("Check now")
	String checkNow();

	@DefaultStringValue("Source will become active as soon as you select region.")
	String selectRegion();

	@DefaultStringValue("Unavailable")
	String unavailable();

	@DefaultStringValue("Yes")
	String watchingYes();

	@DefaultStringValue("Stop watching")
	String unwatchButton();

	@DefaultStringValue("No")
	String watchingNo();

	@DefaultStringValue("Strat watching")
	String watchButton();

	@DefaultStringValue("No check has been performed")
	String notCheckedYet();

	@DefaultStringValue("There were no errors during last check")
	String noErrorSequence();

	@DefaultStringValue("No check has been planned")
	String noCheckPlanned();

	@DefaultStringValue("Custom")
	String customRegion();

	@DefaultStringValue("Positive selector")
	String positiveSelector();

	@DefaultStringValue("Negative selector")
	String negativeSelector();

	@DefaultStringValue("Check region")
	String checkRegion();

	@DefaultStringValue("RSS/Atom")
	String sourceType_RSS_ATOM();

	@DefaultStringValue("Page change")
	String sourceType_PAGE_CHANGE();

	@DefaultStringValue("Website")
	String sourceType_WEB_SITE();

	@DefaultStringValue("Manual")
	String sourceType_MANUAL();

}
