package cz.artique.client.shortcuts;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;

import cz.artique.client.common.StopDialog;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.shortcut.Shortcut;

public class ActionShortcutDialog {

	private static ActionShortcutDialogUiBinder uiBinder = GWT
		.create(ActionShortcutDialogUiBinder.class);

	interface ActionShortcutDialogUiBinder
			extends UiBinder<StopDialog, ActionShortcutDialog> {}

	public static final ActionShortcutDialog DIALOG =
		new ActionShortcutDialog();

	@UiField
	StopDialog dialog;

	@UiField
	ActionShortcutEditor editor;

	@UiField
	Button saveButton;

	@UiField
	Button cancelButton;

	public ActionShortcutDialog() {
		dialog = uiBinder.createAndBindUi(this);
	}

	@UiHandler("saveButton")
	protected void saveButtonClicked(ClickEvent event) {
		Shortcut value = editor.getValue();
		Managers.SHORTCUTS_MANAGER.createShortcut(value,
			new AsyncCallback<Shortcut>() {
				public void onSuccess(Shortcut result) {
					dialog.hide();
					ShortcutsDialog.DIALOG.reload();
				}

				public void onFailure(Throwable caught) {}
			});
	}

	@UiHandler("cancelButton")
	protected void cancelButtonClicked(ClickEvent event) {
		dialog.hide();
	}

	public void showDialog() {
		editor.setValue(null);
		dialog.setWidth("100%");
		dialog.center();
	}
}
