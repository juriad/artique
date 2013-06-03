package cz.artique.client.shortcuts;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;

import cz.artique.client.common.StopDialog;

public class ShortcutsDialog {

	private static ShortcutsDialogUiBinder uiBinder = GWT
		.create(ShortcutsDialogUiBinder.class);

	interface ShortcutsDialogUiBinder
			extends UiBinder<StopDialog, ShortcutsDialog> {}

	public static final ShortcutsDialog DIALOG = new ShortcutsDialog();

	@UiField
	StopDialog dialog;

	@UiField
	ShortcutsEditor editor;

	@UiField
	Button closeButton;

	public ShortcutsDialog() {
		dialog = uiBinder.createAndBindUi(this);
	}

	@UiHandler("closeButton")
	protected void closeButtonClicked(ClickEvent event) {
		dialog.hide();
	}

	public void showDialog() {
		editor.setValue();
		dialog.setWidth("100%");
		dialog.center();
	}

	/**
	 * Called by ActionShortcutDialog
	 */
	public void reload() {
		editor.setValue();
	}
}
