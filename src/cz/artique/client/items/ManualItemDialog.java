package cz.artique.client.items;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;

import cz.artique.client.i18n.Constants;
import cz.artique.client.i18n.I18n;
import cz.artique.client.i18n.Messages;
import cz.artique.client.manager.Managers;
import cz.artique.client.messages.Message;
import cz.artique.client.messages.MessageType;
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
		if (value.getItemObject().getTitle() == null
			|| value.getItemObject().getTitle().trim().isEmpty()) {
			Messages messages = I18n.I18N.getMessages();
			Constants constants = I18n.I18N.getConstants();
			Managers.MESSAGES_MANAGER.addMessage(new Message(MessageType.ERROR,
				messages.errorEmptyField(constants.title())));
			return;
		}

		if (value.getItemObject().getUrl() == null
			|| value.getItemObject().getUrl().getValue().trim().isEmpty()) {
			Messages messages = I18n.I18N.getMessages();
			Constants constants = I18n.I18N.getConstants();
			Managers.MESSAGES_MANAGER.addMessage(new Message(MessageType.ERROR,
				messages.errorEmptyField(constants.url())));
			return;
		}

		Managers.ITEMS_MANAGER.addManualItem(value,
			new AsyncCallback<UserItem>() {

				public void onSuccess(UserItem result) {
					Messages messages = I18n.I18N.getMessages();
					Managers.MESSAGES_MANAGER.addMessage(new Message(
						MessageType.INFO, messages.manualItemCreated(value
							.getItemObject()
							.getTitle())));
					dialog.hide();
				}

				public void onFailure(Throwable caught) {
					Messages messages = I18n.I18N.getMessages();
					Managers.MESSAGES_MANAGER.addMessage(new Message(
						MessageType.INFO, messages
							.errorCreatingManualItem(value
								.getItemObject()
								.getTitle())));
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
