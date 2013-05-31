package cz.artique.client.hierarchy.tree;

import com.google.gwt.i18n.client.ConstantsWithLookup;

public interface HierarchyTreeConstants extends ConstantsWithLookup {

	@DefaultStringValue("Clear all history")
	String clearHistoryTooltip();

	@DefaultStringValue("Clear filter")
	String clearListFilterTooltip();

	@DefaultStringValue("Create new filter")
	String createNewListFilterTooltip();

	@DefaultStringValue("Save current filter")
	String saveCurrentListFilterTooltip();

	@DefaultStringValue("Show filter detail")
	String detailListFilterTooltip();

	@DefaultStringValue("Ad-hoc")
	String adhoc();

	@DefaultStringValue("Adjust filter")
	String adhocTooltip();

	@DefaultStringValue("Clear all messages")
	String clearMessagesTooltip();

	@DefaultStringValue("Show source detail")
	String detailUserSourceTooltip();

	@DefaultStringValue("Insert a new item manually")
	String addManualItemTooltip();

	@DefaultStringValue("Create new source")
	String createNewUserSourceTooltip();

	@DefaultStringValue("Show items of all subsources")
	String sourceFolderTooltip();

	@DefaultStringValue("Hide disabled sources")
	String hideDisabledSourcesTooltip();

	@DefaultStringValue("Show disabled sources")
	String showDisabledSourcesTooltip();

}
