package cz.artique.client.labels;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;

import cz.artique.client.common.StopDialog;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.shortcut.Shortcut;
import cz.artique.shared.model.shortcut.ShortcutType;

public class LabelsDialog {

	private static LabelsDialogUiBinder uiBinder = GWT
		.create(LabelsDialogUiBinder.class);

	interface LabelsDialogUiBinder extends UiBinder<StopDialog, LabelsDialog> {}

	public static final LabelsDialog DIALOG = new LabelsDialog();

	@UiField
	StopDialog dialog;

	@UiField
	LabelsEditor editor;

	@UiField
	Button saveButton;

	@UiField
	Button cancelButton;

	public LabelsDialog() {
		dialog = uiBinder.createAndBindUi(this);
	}

	@UiHandler("saveButton")
	protected void saveButtonClicked(ClickEvent event) {
		List<Label> value = editor.getValue();
		Managers.LABELS_MANAGER.updateLabels(value, null);
		List<Shortcut> toDelete = new ArrayList<Shortcut>();
		List<Shortcut> toCreate = new ArrayList<Shortcut>();
		for (Label l : value) {
			Shortcut s = Managers.SHORTCUTS_MANAGER.getByReferenced(l.getKey());
			Shortcut s2 = null;
			if (l.getShortcutStroke() != null
				&& !l.getShortcutStroke().isEmpty()) {
				s2 = new Shortcut();
				s2.setKeyStroke(l.getShortcutStroke());
				s2.setType(ShortcutType.LABEL);
				s2.setReferenced(l.getKey());
			}

			if (s == null && s2 != null) {
				toCreate.add(s2);
			} else if (s != null && s2 == null) {
				toDelete.add(s);
			} else if (s != null && s2 != null) {
				if (!s.getKeyStroke().equals(s2.getKeyStroke())) {
					toCreate.add(s2);
					toDelete.add(s);
				}
			}
		}
		for (Shortcut del : toDelete) {
			Managers.SHORTCUTS_MANAGER.deleteShortcut(del, null);
		}
		for (Shortcut cre : toCreate) {
			Managers.SHORTCUTS_MANAGER.createShortcut(cre, null);
		}

		dialog.hide();
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
