package cz.artique.shared.model.shortcut;

import cz.artique.client.shortcuts.ShortcutsConstants;

/**
 * Lists all possible types of shortcuts.
 * 
 * This enum also provides (method {@link #name()}) part of name for
 * internationalization
 * in {@link ShortcutsConstants}.
 * 
 * @author Adam Juraszek
 * 
 */
public enum ShortcutType {
	/**
	 * Adds (or removes when already present) label to the selected item.
	 */
	LABEL,
	/**
	 * Changes list of displayed items in listing to those which meets criteria
	 * of referenced list filter.
	 */
	LIST_FILTER,
	/**
	 * Performs an action defined in {@link ShortcutAction} enum.
	 */
	ACTION;
}
