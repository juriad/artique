package cz.artique.client.listFilters;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;

import cz.artique.client.common.UniversalDialog;
import cz.artique.client.i18n.I18n;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.label.ListFilter;
import cz.artique.shared.model.shortcut.Shortcut;
import cz.artique.shared.model.shortcut.ShortcutType;

/**
 * Shows named persistent {@link ListFilter}.
 * This dialog allows user to create, edit and delete any {@link ListFilter}.
 * 
 * @author Adam Juraszek
 * 
 */
public class ListFilterDialog extends UniversalDialog<ListFilter> {

	public static final ListFilterDialog DIALOG = new ListFilterDialog();

	public ListFilterDialog() {
		final ListFiltersConstants constants = I18n.getListFiltersConstants();
		setText(constants.listFilterDialog());
		final ListFilterEditor editor = new ListFilterEditor(true);
		setWidget(editor);

		final Button deleteButton = new Button(constants.deleteButton());
		deleteButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				final ListFilter value = editor.getValue();
				if (value.getKey() != null) {
					// delete
					Managers.LIST_FILTERS_MANAGER.deleteListFilter(value, null);
					hide();
				} else {
					// wipe
					editor.setValue(new ListFilter());
					deleteButton.setText(constants.wipeButton());
				}
			}
		});

		addButton(deleteButton);

		addButton(constants.saveButton(), new ClickHandler() {
			public void onClick(ClickEvent event) {
				final ListFilter value = editor.getValue();
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
									Managers.SHORTCUTS_MANAGER.createShortcut(
										s, null);
								}
							}

							public void onFailure(Throwable caught) {
								// silently discard shortcut
							}
						});
				} else {
					Managers.LIST_FILTERS_MANAGER.updateListFilter(value, null);
					Shortcut s =
						Managers.SHORTCUTS_MANAGER.getByReferenced(value
							.getKey());
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
				hide();
			}
		});

		addButton(constants.cancelButton(), HIDE);

		setShowAction(new OnShowAction<ListFilter>() {
			public boolean onShow(ListFilter param) {
				editor.setValue(param);
				if (param == null || param.getKey() == null) {
					deleteButton.setText(constants.wipeButton());
				} else {
					deleteButton.setText(constants.deleteButton());
				}
				return true;
			}
		});
	}
}
