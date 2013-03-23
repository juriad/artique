package cz.artique.client.artiqueListFilters;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;

import cz.artique.client.artiqueHistory.ArtiqueHistory;
import cz.artique.client.i18n.ArtiqueConstants;
import cz.artique.client.i18n.ArtiqueI18n;
import cz.artique.shared.model.label.ListFilter;

public class ListFilterDialog extends Composite {

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

	protected void newButtonClicked(ClickEvent event) {
		editor.setValue(new ListFilter());
		setNewButtonText();
	}

	protected void cloneButtonClicked(ClickEvent event) {
		editor.setListFilterKey(null);
		editor.setFilterKey(null);
		setNewButtonText();
	}

	protected void saveButtonClicked(ClickEvent event) {
		// TODO as soon as messages are completed
		dialog.hide();
	}

	protected void deleteButtonClicked(ClickEvent event) {
		// TODO as soon as messages are completed
		dialog.hide();
	}

	protected void cancelButtonClicked(ClickEvent event) {
		// TODO as soon as messages are completed
		dialog.hide();
	}

	public void showDialog() {
		if (dialog == null) {
			createDialog();
		}
		editor.setValue(ArtiqueHistory.HISTORY
			.getLastHistoryItem()
			.getListFilter());
		setNewButtonText();
		dialog.show();
	}

	public void setNewButtonText() {
		ArtiqueConstants constants = ArtiqueI18n.I18N.getConstants();
		if (editor.getListFilterKey() != null) {
			newButton.setText(constants.newButton());
			cloneButton.setEnabled(false);
			deleteButton.setEnabled(false);
		} else {
			newButton.setText(constants.wipeButton());
			cloneButton.setEnabled(true);
			deleteButton.setEnabled(true);
		}
	}

	private void createDialog() {
		dialog = new DialogBox(true, true);
		dialog.setWidth("50%");
		dialog.setHeight("50%");
	}

	public ListFilter getListFilter() {
		return editor.getValue();
	}
}
