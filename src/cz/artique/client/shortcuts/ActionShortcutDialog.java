package cz.artique.client.shortcuts;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.common.UniversalDialog;
import cz.artique.client.i18n.I18n;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.shortcut.Shortcut;

/**
 * Dialog shown to user when he wants to add a new action shortcut.
 * 
 * @author Adam Juraszek
 * 
 */
public class ActionShortcutDialog extends UniversalDialog<Void> {

	public static final ActionShortcutDialog DIALOG =
		new ActionShortcutDialog();

	public ActionShortcutDialog() {
		ShortcutsConstants constants = I18n.getShortcutsConstants();
		setText(constants.actionShortcutDialog());
		final ActionShortcutEditor editor = new ActionShortcutEditor();
		setWidget(editor);

		addButton(constants.saveButton(), new ClickHandler() {
			public void onClick(ClickEvent event) {
				Shortcut value = editor.getValue();
				Managers.SHORTCUTS_MANAGER.createShortcut(value,
					new AsyncCallback<Shortcut>() {
						public void onSuccess(Shortcut result) {
							hide();
							ShortcutsDialog.DIALOG.showDialog();
						}

						public void onFailure(Throwable caught) {}
					});
			}
		});

		addButton(constants.cancelButton(), HIDE);

		setShowAction(new OnShowAction<Void>() {
			public boolean onShow(Void param) {
				editor.setValue(null);
				return true;
			}
		});
	}
}
