package cz.artique.client.sources;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.common.UniversalDialog;
import cz.artique.client.i18n.I18n;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.source.UserSource;

public class UserSourceDialog extends UniversalDialog<UserSource> {

	public static final UserSourceDialog DIALOG = new UserSourceDialog();

	public UserSourceDialog() {
		SourcesConstants constants = I18n.getSourcesConstants();
		setText(constants.userSourceDialog());
		final UserSourceEditor editor = new UserSourceEditor();
		setWidget(editor);

		addButton(constants.saveButton(), new ClickHandler() {
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
