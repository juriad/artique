package cz.artique.client.artiqueListFilters;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;

import cz.artique.client.artiqueHistory.CachingHistoryUtils;
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
	Button deleteButton;

	@UiField
	Button saveButton;

	@UiField
	Button cancelButton;

	private boolean proper;

	public ListFilterDialog() {
		dialog = uiBinder.createAndBindUi(this);
	}

	@UiHandler("deleteButton")
	protected void deleteButtonClicked(ClickEvent event) {
		final ListFilter value = editor.getValue();
		if (value.getKey() != null) {
			// delete
			Managers.LIST_FILTERS_MANAGER.deleteListFilter(value, null);
			dialog.hide();
		} else {
			// wipe
			editor.setValue(new ListFilter());
			setButtons(false);
		}
	}

	@UiHandler("saveButton")
	protected void saveButtonClicked(ClickEvent event) {
		final ListFilter value = editor.getValue();
		if (proper) {
			if (value.getName() == null) {
				ArtiqueMessages messages = ArtiqueI18n.I18N.getMessages();
				ArtiqueConstants constants = ArtiqueI18n.I18N.getConstants();
				Managers.MESSAGES_MANAGER.addMessage(new Message(
					MessageType.ERROR, messages.errorEmptyField(constants
						.name())));
				return;
			}

			if (value.getKey() == null) {
				Managers.LIST_FILTERS_MANAGER.addListFilter(value, null);
			} else {
				Managers.LIST_FILTERS_MANAGER.updateListFilter(value, null);
			}
		} else {
			String token = CachingHistoryUtils.UTILS.serializeListFilter(value);
			Managers.HISTORY_MANAGER.setListFilter(value, token);
		}
		dialog.hide();
	}

	@UiHandler("cancelButton")
	protected void cancelButtonClicked(ClickEvent event) {
		dialog.hide();
	}

	public void showDialog(ListFilter value, boolean proper) {
		this.proper = proper;
		editor.setValue(value);
		editor.setProper(proper);
		setButtons(value.getKey() != null);
		dialog.setWidth("100%");
		dialog.center();
	}

	public void setButtons(boolean justEditing) {
		ArtiqueConstants constants = ArtiqueI18n.I18N.getConstants();
		if (justEditing) {
			deleteButton.setText(constants.deleteButton());
		} else {
			deleteButton.setText(constants.wipeButton());
		}

		if (proper) {
			saveButton.setText(constants.saveButton());
			cancelButton.setText(constants.cancelButton());
		} else {
			saveButton.setText(constants.applyButton());
			cancelButton.setText(constants.closeButton());
		}
	}

	public ListFilter getListFilter() {
		return editor.getValue();
	}
}
