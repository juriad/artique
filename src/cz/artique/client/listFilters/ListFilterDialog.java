package cz.artique.client.listFilters;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;

import cz.artique.client.common.StopDialog;
import cz.artique.client.history.CachingHistoryUtils;
import cz.artique.client.i18n.I18n;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.label.ListFilter;
import cz.artique.shared.model.shortcut.Shortcut;
import cz.artique.shared.model.shortcut.ShortcutType;

public class ListFilterDialog {

	private static ListFilterDialogUiBinder uiBinder = GWT
		.create(ListFilterDialogUiBinder.class);

	interface ListFilterDialogUiBinder
			extends UiBinder<StopDialog, ListFilterDialog> {}

	public static final ListFilterDialog DIALOG = new ListFilterDialog();

	@UiField
	StopDialog dialog;

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
			if (value.getKey() == null) {
				Managers.LIST_FILTERS_MANAGER.addListFilter(value,
					new AsyncCallback<ListFilter>() {
						public void onSuccess(ListFilter result) {
							if (value.getShortcutStroke() != null
								&& !value.getShortcutStroke().isEmpty()) {
								Shortcut s = new Shortcut();
								s.setKeyStroke(value.getShortcutStroke());
								s.setType(ShortcutType.LIST_FILTER);
								s.setReferenced(result.getKey());
								Managers.SHORTCUTS_MANAGER.createShortcut(s,
									null);
							}
						}

						public void onFailure(Throwable caught) {
							// silently discard shortcut
						}
					});
			} else {
				Managers.LIST_FILTERS_MANAGER.updateListFilter(value, null);
				Shortcut s =
					Managers.SHORTCUTS_MANAGER.getByReferenced(value.getKey());
				Shortcut s2 = null;
				if (value.getShortcutStroke() != null
					&& !value.getShortcutStroke().isEmpty()) {
					s2 = new Shortcut();
					s2.setKeyStroke(value.getShortcutStroke());
					s2.setType(ShortcutType.LIST_FILTER);
					s2.setReferenced(value.getKey());
				}
				if (s == null && s2 != null) {
					Managers.SHORTCUTS_MANAGER.createShortcut(s2, null);
				} else if (s != null && s2 == null) {
					Managers.SHORTCUTS_MANAGER.deleteShortcut(s, null);
				} else if (s != null && s2 != null) {
					if (!s.getKeyStroke().equals(s2.getKeyStroke())) {
						Managers.SHORTCUTS_MANAGER.deleteShortcut(s, null);
						Managers.SHORTCUTS_MANAGER.createShortcut(s2, null);
					}
				}
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
		ListFiltersConstants constants = I18n.getListFiltersConstants();
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
}
