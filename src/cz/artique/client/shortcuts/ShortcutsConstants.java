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
}
