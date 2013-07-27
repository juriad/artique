package cz.artique.client.items;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.common.UniversalDialog;
import cz.artique.client.i18n.I18n;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.item.UserItem;

/**
 * Dialog which is shown when user wants to add new manual item.
 * 
 * @author Adam Juraszek
 * 
 */
public class ManualItemDialog extends UniversalDialog<Void> {

	public static final ManualItemDialog DIALOG = new ManualItemDialog();

	public ManualItemDialog() {
		ItemsConstants constants = I18n.getItemsConstants();
		setText(constants.manualItemDialog());
		final ManualItemEditor editor = new ManualItemEditor();
		setWidget(editor);

		addButton(constants.saveButton(), new ClickHandler() {
			public void onClick(ClickEvent event) {
				final UserItem value = editor.getValue();
				Managers.ITEMS_MANAGER.addManualItem(value,
					new AsyncCallback<UserItem>() {
						public void onSuccess(UserItem result) {
							hide();
						}

						public void onFailure(Throwable caught) {}
					});
			}
		});

		addButton(constants.cancelButton(), HIDE);

		setShowAction(new OnShowAction<Void>() {
			public boolean onShow(Void param) {
				editor.setValue(null);
				return true;
			}
		});
	}
}
