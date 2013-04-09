package cz.artique.client.artiqueListFilters;

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
import cz.artique.shared.model.label.ListFilter;

public class ListFilterDialog {

	private static ListFilterDialogUiBinder uiBinder = GWT
		.create(ListFilterDialogUiBinder.class);

	interface ListFilterDialogUiBinder
			extends UiBinder<DialogBox, ListFilterDialog> {}

	public static final ListFilterDialog DIALOG = new ListFilterDialog();

	@UiField
	DialogBox dialog;

	@UiField
	ListFilterEditor editor;

	@UiField
	Button newButton;

	@UiField
	Button cloneButton;

	@UiField
	Button saveButton;

	@UiField
	Button deleteButton;

	@UiField
	Button cancelButton;

	public ListFilterDialog() {
		dialog = uiBinder.createAndBindUi(this);
	}

	@UiHandler("newButton")
	protected void newButtonClicked(ClickEvent event) {
		editor.setValue(new ListFilter());
		setNewButtonText(false);
	}

	@UiHandler("cloneButton")
	protected void cloneButtonClicked(ClickEvent event) {
		ListFilter value = editor.getValue();
		value.setFilter(null);
		value.setKey(null);
		value.setExportAlias(null);
		editor.setValue(value);
		setNewButtonText(false);
	}

	@UiHandler("saveButton")
	protected void saveButtonClicked(ClickEvent event) {
		final ListFilter value = editor.getValue();

		if (value.getName() == null) {
			ArtiqueMessages messages = ArtiqueI18n.I18N.getMessages();
			ArtiqueConstants constants = ArtiqueI18n.I18N.getConstants();
			Managers.MESSAGES_MANAGER.addMessage(new Message(MessageType.ERROR,
				messages.errorEmptyField(constants.name())));
			return;
		}

		if (value.getKey() == null) {
			Managers.LIST_FILTERS_MANAGER.addListFilter(value, null);
		} else {
			Managers.LIST_FILTERS_MANAGER.updateListFilter(value, null);
		}
		dialog.hide();
	}

	@UiHandler("deleteButton")
	protected void deleteButtonClicked(ClickEvent event) {
		final ListFilter value = editor.getValue();
		Managers.LIST_FILTERS_MANAGER.deleteListFilter(value, null);
		dialog.hide();
	}

	@UiHandler("cancelButton")
	protected void cancelButtonClicked(ClickEvent event) {
		dialog.hide();
	}

	public void showDialog(ListFilter value) {
		editor.setValue(value);
		setNewButtonText(value.getKey() != null);
		dialog.show();
	}

	public void setNewButtonText(boolean justEditing) {
		ArtiqueConstants constants = ArtiqueI18n.I18N.getConstants();
		if (justEditing) {
			newButton.setText(constants.newButton());
			cloneButton.setEnabled(true);
			deleteButton.setEnabled(true);
		} else {
			newButton.setText(constants.wipeButton());
			cloneButton.setEnabled(false);
			deleteButton.setEnabled(false);
		}
	}

	public ListFilter getListFilter() {
		return editor.getValue();
	}
}
