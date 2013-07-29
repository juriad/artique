package cz.artique.client.shortcuts;

import cz.artique.client.common.UniversalDialog;
import cz.artique.client.i18n.I18n;

/**
 * Dialog shown to user when he wants to review list of all defined shortcuts.
 * The user may delete a shortcut or trigger {@link ActionShortcutDialog} to be
 * shown.
 * 
 * @author Adam Juraszek
 * 
 */
public class ShortcutsDialog extends UniversalDialog<Void> {

	public static final ShortcutsDialog DIALOG = new ShortcutsDialog();

	public ShortcutsDialog() {
		ShortcutsConstants constants = I18n.getShortcutsConstants();
		setText(constants.actionShortcutDialog());
		final ShortcutsEditor editor = new ShortcutsEditor();
		setWidget(editor);

		addButton(constants.closeButton(), HIDE);

		setShowAction(new OnShowAction<Void>() {
			public boolean onShow(Void param) {
				editor.setValue();
				return true;
			}
		});
	}
}
