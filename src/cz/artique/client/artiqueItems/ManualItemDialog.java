package cz.artique.client.artiqueItems;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;

import cz.artique.client.i18n.ArtiqueConstants;
import cz.artique.client.i18n.ArtiqueI18n;
import cz.artique.client.i18n.ArtiqueMessages;
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
			ArtiqueMessages messages = ArtiqueI18n.I18N.getMessages();
			ArtiqueConstants constants = ArtiqueI18n.I18N.getConstants();
			Managers.MESSAGES_MANAGER.addMessage(new Message(MessageType.ERROR,
				messages.errorEmptyField(constants.title())));
			return;
		}

		if (value.getItemObject().getUrl() == null
			|| value.getItemObject().getUrl().getValue().trim().isEmpty()) {
			ArtiqueMessages messages = ArtiqueI18n.I18N.getMessages();
			ArtiqueConstants constants = ArtiqueI18n.I18N.getConstants();
			Managers.MESSAGES_MANAGER.addMessage(new Message(MessageType.ERROR,
				messages.errorEmptyField(constants.url())));
			return;
		}

		Managers.ITEMS_MANAGER.addManualItem(value,
			new AsyncCallback<UserItem>() {

				public void onSuccess(UserItem result) {
					ArtiqueMessages messages = ArtiqueI18n.I18N.getMessages();
					Managers.MESSAGES_MANAGER.addMessage(new Message(
						MessageType.INFO, messages.manualItemCreated(value
							.getItemObject()
							.getTitle())));
					dialog.hide();
				}

				public void onFailure(Throwable caught) {
					ArtiqueMessages messages = ArtiqueI18n.I18N.getMessages();
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
