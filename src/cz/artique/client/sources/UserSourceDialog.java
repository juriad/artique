package cz.artique.client.sources;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;

import cz.artique.client.common.UniversalDialog;
import cz.artique.client.i18n.I18n;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.UserSource;

/**
 * Dialog shown in three cases: when user wants create custom {@link UserSource}
 * , when user wants to watch recommended {@link Source}, when user wants to
 * edit an existing {@link UserSource}.
 * 
 * @author Adam Juraszek
 * 
 */
public class UserSourceDialog extends UniversalDialog<UserSource> {

	public static final UserSourceDialog DIALOG = new UserSourceDialog();

	public UserSourceDialog() {
		SourcesConstants constants = I18n.getSourcesConstants();
		setText(constants.userSourceDialog());

		final Button setUrlButton = addButton(constants.setUrl(), null);
		final Button saveButton = addButton(constants.saveButton(), null);

		final UserSourceEditor editor =
			new UserSourceEditor(setUrlButton, saveButton);
		setWidget(editor);

		setUrlButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				editor.setUrlButtonClicked();
			}
		});

		saveButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				final UserSource value = editor.getValue();
				if (value.getKey() == null) {
					Managers.SOURCES_MANAGER.addUserSource(value,
						new AsyncCallback<UserSource>() {
							public void onSuccess(UserSource result) {
								hide();
							}

							public void onFailure(Throwable caught) {}
						});
				} else {
					Managers.SOURCES_MANAGER.updateUserSource(value,
						new AsyncCallback<UserSource>() {
							public void onSuccess(UserSource result) {
								hide();
							}

							public void onFailure(Throwable caught) {}
						});
				}
			}
		});

		addButton(constants.cancelButton(), HIDE);

		setShowAction(new OnShowAction<UserSource>() {
			public boolean onShow(UserSource param) {
				if (param == null) {
					return false;
				}
				editor.setValue(param);
				return true;
			}
		});
	}
}
