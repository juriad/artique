package cz.artique.shared.model.shortcut;

import cz.artique.client.shortcuts.ActionPicker;
import cz.artique.client.shortcuts.ShortcutsConstants;

/**
 * Lists all possible actions for {@link Shortcut} when type is
 * {@link ShortcutType#ACTION}.
 * 
 * This enum also lists all possible states of {@link ActionPicker}. And
 * also provides (method {@link #name()}) part of name for internationalization
 * in {@link ShortcutsConstants}.
 * 
 * @author Adam Juraszek
 * 
 */
public enum ShortcutAction {
	/**
	 * Reload list of items for current list filter.
	 */
	REFRESH,
	/**
	 * Marks all items above the selected one as read.
	 */
	MARK_READ_ALL,
	/**
	 * Toggles collapsed/expanded state on item.
	 */
	TOGGLE_COLLAPSED,
	/**
	 * Moves selection to the next item.
	 */
	NEXT_ITEM,
	/**
	 * Moves selection to the previous item.
	 */
	PREVIOUS_ITEM,
	/**
	 * Opens original version of item in a new tab.
	 */
	OPEN_ORIGINAL,
	/**
	 * Opens input box on selected item to add (or remove) label.
	 */
	ADD_LABEL,
	/**
	 * Clears current filter part of list filter leaving date and read criteria
	 * set.
	 */
	CLEAR_FILTER,
	/**
	 * Clears all criteria on current list filter.
	 */
	TOTAL_CLEAR_FILTER,

	/**
	 * Shows ad-hoc filter dialog.
	 */
	ADJUST_FILTER,
	/**
	 * Prepends new items to the top of current listing of items.
	 */
	ADD_NEW_ITEMS,
	/**
	 * Opens dialog which allows user to add his manual item to system to manual
	 * source.
	 */
	ADD_MANUAL_ITEM;
}
