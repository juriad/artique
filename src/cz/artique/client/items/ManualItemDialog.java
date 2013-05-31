package cz.artique.client.items;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;

import cz.artique.client.manager.Managers;
import cz.artique.shared.model.item.UserItem;

public class ManualItemDialog {

	private static ManualItemDialogUiBinder uiBinder = GWT
		.create(ManualItemDialogUiBinder.class);

	interface ManualItemDialogUiBinder
			extends UiBinder<DialogBox, ManualItemDialog> {}

	public static final ManualItemDialog DIALOG = new ManualItemDialog();

	@UiField
	DialogBox dialog;

	@UiField
	ManualItemEditor editor;

	@UiField
	Button saveButton;

	@UiField
	Button cancelButton;

	public ManualItemDialog() {
		dialog = uiBinder.createAndBindUi(this);
	}

	@UiHandler("saveButton")
	protected void saveButtonClicked(ClickEvent event) {
		final UserItem value = editor.getValue();
		Managers.ITEMS_MANAGER.addManualItem(value,
			new AsyncCallback<UserItem>() {

				public void onSuccess(UserItem result) {
					dialog.hide();
				}

				public void onFailure(Throwable caught) {
					// TODO 
				}
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
