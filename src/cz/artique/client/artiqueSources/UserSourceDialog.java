package cz.artique.client.artiqueSources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;

import cz.artique.client.i18n.ArtiqueConstants;
import cz.artique.client.i18n.ArtiqueI18n;
import cz.artique.client.i18n.ArtiqueMessages;
import cz.artique.client.manager.Managers;
import cz.artique.client.messages.Message;
import cz.artique.client.messages.MessageType;
import cz.artique.shared.model.source.UserSource;

public class UserSourceDialog {

	private static UserSourceDialogUiBinder uiBinder = GWT
		.create(UserSourceDialogUiBinder.class);

	interface UserSourceDialogUiBinder
			extends UiBinder<DialogBox, UserSourceDialog> {}

	public static final UserSourceDialog DIALOG = new UserSourceDialog();

	@UiField
	DialogBox dialog;

	@UiField
	UserSourceEditor editor;

	@UiField
	Button saveButton;

	@UiField
	Button cancelButton;

	public UserSourceDialog() {
		dialog = uiBinder.createAndBindUi(this);
	}

	@UiHandler("saveButton")
	protected void saveButtonClicked(ClickEvent event) {
		final UserSource value = editor.getValue();
		if (value.getName() == null || value.getName().trim().isEmpty()) {
			ArtiqueMessages messages = ArtiqueI18n.I18N.getMessages();
			ArtiqueConstants constants = ArtiqueI18n.I18N.getConstants();
			Managers.MESSAGES_MANAGER.addMessage(new Message(MessageType.ERROR,
				messages.errorEmptyField(constants.name())));
			return;
		}

		if (value.getKey() == null) {
			Managers.SOURCES_MANAGER.addUserSource(value, null);
		} else {
			Managers.SOURCES_MANAGER.updateUserSource(value, null);
		}
		dialog.hide();
	}

	@UiHandler("cancelButton")
	protected void cancelButtonClicked(ClickEvent event) {
		dialog.hide();
	}

	public void showDialog(UserSource value) {
		editor.setValue(value);
		dialog.setWidth("100%");
		dialog.center();
	}
}
