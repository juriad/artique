package cz.artique.client.labels;

import com.google.gwt.i18n.client.ConstantsWithLookup;

public interface LabelsConstants extends ConstantsWithLookup {
	@DefaultStringValue("AND")
	String operatorAnd();

	@DefaultStringValue("OR")
	String operatorOr();

	@DefaultStringValue("Labels editor")
	String labelsEditor();

	@DefaultStringValue("Save")
	String saveButton();

	@DefaultStringValue("Cancel")
	String cancelButton();

	@DefaultStringValue("Name")
	String name();

	@DefaultStringValue("Foreground color")
	String foregroundColor();

	@DefaultStringValue("Background color")
	String backgroundColor();

	@DefaultStringValue("Marked to be deleted")
	String markedToDelete();

	@DefaultStringValue("Yes")
	String deleteYes();

	@DefaultStringValue("No")
	String deleteNo();

	@DefaultStringValue("Mark")
	String mark();

	@DefaultStringValue("Unmark")
	String unmark();

	@DefaultStringValue("Backup level")
	String backupLevel();

	@DefaultStringValue("Disabled")
	String backupLevel_NO_BACKUP();

	@DefaultStringValue("HTML")
	String backupLevel_HTML();

	@DefaultStringValue("HTML & CSS")
	String backupLevel_HTML_CSS();

	@DefaultStringValue("Shortcut")
	String shortcut();

	@DefaultStringValue("No label exists.")
	String noLabelExists();

}
