package cz.artique.client.shortcuts;

import cz.artique.client.common.UniversalDialog;
import cz.artique.client.i18n.I18n;

public class ShortcutsDialog extends UniversalDialog<Void> {

	public static final ShortcutsDialog DIALOG = new ShortcutsDialog();

	public ShortcutsDialog() {
		ShortcutsConstants constants = I18n.getShortcutsConstants();
		setText(constants.actionShortcutDialog());
		final ShortcutsEditor editor = new ShortcutsEditor();
		setWidget(editor);

		addButton(constants.closeButton(), HIDE);

		setShowAction(new OnShowAction<Void>() {
			public void onShow(Void param) {
				editor.setValue();
			}
		});
	}
}
