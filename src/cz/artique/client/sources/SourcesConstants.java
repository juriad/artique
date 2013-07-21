package cz.artique.client.sources;

import com.google.gwt.i18n.client.ConstantsWithLookup;

public interface SourcesConstants extends ConstantsWithLookup {

	@DefaultStringValue("Source editor")
	String userSourceDialog();

	@DefaultStringValue("Save")
	String saveButton();

	@DefaultStringValue("Cancel")
	String cancelButton();

	@DefaultStringValue("Source type")
	String sourceType();

	@DefaultStringValue("URL")
	String url();

	@DefaultStringValue("Fix URL and continue")
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

	@DefaultStringValue("Errors")
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
	String watchingYesHover();

	@DefaultStringValue("No")
	String watchingNo();

	@DefaultStringValue("Start watching")
	String watchingNoHover();

	@DefaultStringValue("No check has been performed")
	String notCheckedYet();

	@DefaultStringValue("No errors")
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

	@DefaultStringValue("Recommended sources")
	String recommendedEditor();

	@DefaultStringValue("Close")
	String closeButton();

	@DefaultStringValue("Domain")
	String domain();

	@DefaultStringValue("Watch source")
	String watchSourceButton();

	@DefaultStringValue("New source")
	String sourceSource();

	@DefaultStringValue("Custom")
	String sourceSource_CUSTOM();

	@DefaultStringValue("Recommended")
	String sourceSource_RECOMMENDED();

	@DefaultStringValue("No recommendation available.")
	String noRecommendation();

}
