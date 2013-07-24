package cz.artique.client.labels;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import cz.artique.client.common.UniversalDialog;
import cz.artique.client.i18n.I18n;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.shortcut.Shortcut;
import cz.artique.shared.model.shortcut.ShortcutType;

public class LabelsDialog extends UniversalDialog<Void> {

	public static final LabelsDialog DIALOG = new LabelsDialog();

	public LabelsDialog() {
		LabelsConstants constants = I18n.getLabelsConstants();
		setText(constants.labelsDialog());
		final LabelsEditor editor = new LabelsEditor();
		setWidget(editor);

		addButton(constants.saveButton(), new ClickHandler() {
			public void onClick(ClickEvent event) {
				List<Label> value = editor.getValue();
				Managers.LABELS_MANAGER.updateLabels(value, null);
				List<Shortcut> toDelete = new ArrayList<Shortcut>();
				List<Shortcut> toCreate = new ArrayList<Shortcut>();
				for (Label l : value) {
					Shortcut s =
						Managers.SHORTCUTS_MANAGER.getByReferenced(l.getKey());
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

				hide();
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
