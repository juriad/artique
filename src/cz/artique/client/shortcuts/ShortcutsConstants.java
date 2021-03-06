package cz.artique.client.shortcuts;

import com.google.gwt.i18n.client.ConstantsWithLookup;

public interface ShortcutsConstants extends ConstantsWithLookup {
	@DefaultStringValue("Shortcuts editor")
	String shortcutsEditor();

	@DefaultStringValue("Close")
	String closeButton();

	@DefaultStringValue("Key stroke")
	String keyStroke();

	@DefaultStringValue("Type")
	String type();

	@DefaultStringValue("Action")
	String referencedAction();

	@DefaultStringValue("Object")
	String referencedObject();

	@DefaultStringValue("Delete")
	String deleteButton();

	@DefaultStringValue("Switch to filter")
	String shortcutType_LIST_FILTER();

	@DefaultStringValue("Toggle label")
	String shortcutType_LABEL();

	@DefaultStringValue("Action")
	String shortcutType_ACTION();

	@DefaultStringValue("Add action shortcut")
	String addActionShortcut();

	@DefaultStringValue("Action shortcut editor")
	String actionShortcutDialog();

	@DefaultStringValue("Save")
	String saveButton();

	@DefaultStringValue("Cancel")
	String cancelButton();

	@DefaultStringValue("Action")
	String shortcutAction();

	@DefaultStringValue("Refresh")
	String shortcutAction_REFRESH();

	@DefaultStringValue("Mark read until here")
	String shortcutAction_MARK_READ_ALL();

	@DefaultStringValue("Toggle collapsed")
	String shortcutAction_TOGGLE_COLLAPSED();

	@DefaultStringValue("Move to next item")
	String shortcutAction_NEXT_ITEM();

	@DefaultStringValue("Move to previous item")
	String shortcutAction_PREVIOUS_ITEM();

	@DefaultStringValue("Open original")
	String shortcutAction_OPEN_ORIGINAL();

	@DefaultStringValue("Clear label filter")
	String shortcutAction_CLEAR_FILTER();

	@DefaultStringValue("Clear whole filter")
	String shortcutAction_TOTAL_CLEAR_FILTER();

	@DefaultStringValue("Adjust filter")
	String shortcutAction_ADJUST_FILTER();

	@DefaultStringValue("Add label")
	String shortcutAction_ADD_LABEL();

	@DefaultStringValue("Add new items")
	String shortcutAction_ADD_NEW_ITEMS();

	@DefaultStringValue("Add manual item")
	String shortcutAction_ADD_MANUAL_ITEM();

	@DefaultStringValue("No defined shortcut.")
	String noDefinedShortcut();
}
