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

	@DefaultStringValue("Referenced object")
	String referenced();

	@DefaultStringValue("Delete")
	String deleteButton();

	@DefaultStringValue("Switch to filter")
	String shortcut_LIST_FILTER();

	@DefaultStringValue("Toggle label")
	String shortcut_LABEL();

	@DefaultStringValue("Action")
	String shortcut_ACTION();

	@DefaultStringValue("Add action shortcut")
	String addActionShortcut();

	@DefaultStringValue("Action shortcut editor")
	String actionShortcutEditor();

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
}
