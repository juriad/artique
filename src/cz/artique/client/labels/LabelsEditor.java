package cz.artique.client.labels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;

import cz.artique.client.common.ColorInput;
import cz.artique.client.common.ScrollableCellList;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.label.BackupLevel;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.LabelAppearance;
import cz.artique.shared.model.label.LabelType;
import cz.artique.shared.model.shortcut.Shortcut;

/**
 * Editor shown inside {@link LabelsDialog}; defines layout and control.
 * 
 * @author Adam Juraszek
 * 
 */
public class LabelsEditor extends Composite implements HasValue<List<Label>> {
	private static LabelsEditorUiBinder uiBinder = GWT
		.create(LabelsEditorUiBinder.class);

	interface LabelsEditorUiBinder extends UiBinder<Widget, LabelsEditor> {}

	class LabelCell extends AbstractCell<Label> {

		public LabelCell() {}

		@Override
		public void render(Context context, Label value, SafeHtmlBuilder sb) {
			// Value can be null, so do a null check..
			if (value == null) {
				return;
			}

			sb.appendHtmlConstant("<span class='labelCell'>");
			sb.appendEscaped(value.getDisplayName());
			sb.appendHtmlConstant("</span>");
		}
	}

	@UiField(provided = true)
	ScrollableCellList<Label> cellList;

	@UiField
	InlineLabel name;

	@UiField
	ColorInput foregroundColor;

	@UiField
	ColorInput backgroundColor;

	@UiField
	ToggleButton markedToDelete;

	@UiField
	BackupLevelPicker backupLevel;

	@UiField
	TextBox shortcut;

	private Map<Key, Label> changes = new HashMap<Key, Label>();

	public LabelsEditor() {
		cellList = new ScrollableCellList<Label>(new LabelCell());
		initWidget(uiBinder.createAndBindUi(this));
		cellList.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				setFields();
			}
		});
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<List<Label>> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	/**
	 * When user selects foreground color, add the color selection to list of
	 * changes.
	 * 
	 * @param event
	 *            event
	 */
	@UiHandler("foregroundColor")
	protected void foregroundColorChanged(ValueChangeEvent<String> event) {
		Label selected = getLabel(cellList.getSelected());
		if (selected == null) {
			return;
		}
		selected.getAppearance().setForegroundColor(event.getValue());
		changes.put(selected.getKey(), selected);
		setFields();
	}

	/**
	 * When user selects background color, add the color selection to list of
	 * changes.
	 * 
	 * @param event
	 *            event
	 */
	@UiHandler("backgroundColor")
	protected void backgroundColorChanged(ValueChangeEvent<String> event) {
		Label selected = getLabel(cellList.getSelected());
		if (selected == null) {
			return;
		}
		selected.getAppearance().setBackgroundColor(event.getValue());
		changes.put(selected.getKey(), selected);
		setFields();
	}

	/**
	 * When user marks (unmarks) the {@link Label} to be deleted, add the new
	 * state to list of changes.
	 * 
	 * @param event
	 *            event
	 */
	@UiHandler("markedToDelete")
	protected void markedToDeleteChanged(ValueChangeEvent<Boolean> event) {
		Label selected = getLabel(cellList.getSelected());
		if (selected == null) {
			return;
		}
		selected.setToBeDeleted(event.getValue());
		changes.put(selected.getKey(), selected);
		setFields();
	}

	/**
	 * When user changes backup level of the {@link Label}, add the new
	 * state to list of changes.
	 * 
	 * @param event
	 *            event
	 */
	@UiHandler("backupLevel")
	protected void backupLevelChanged(ValueChangeEvent<BackupLevel> event) {
		Label selected = getLabel(cellList.getSelected());
		if (selected == null) {
			return;
		}
		selected.setBackupLevel(event.getValue());
		changes.put(selected.getKey(), selected);
		setFields();
	}

	/**
	 * When user changes (or adds or removes) shortcut to the {@link Label}, add
	 * the new shortcut to list of changes.
	 * 
	 * @param event
	 *            event
	 */
	@UiHandler("shortcut")
	protected void shortcutChanged(ValueChangeEvent<String> event) {
		Label selected = getLabel(cellList.getSelected());
		if (selected == null) {
			return;
		}
		selected.setShortcutStroke(event.getValue());
		changes.put(selected.getKey(), selected);
		setFields();
	}

	/**
	 * Returns list of changed {@link Label}s.
	 * 
	 * @see com.google.gwt.user.client.ui.HasValue#getValue()
	 */
	public List<Label> getValue() {
		List<Label> result = new ArrayList<Label>(changes.values());
		return result;
	}

	/**
	 * Fills the dialog with list of all user-defined {@link Label}s.
	 * 
	 * @see com.google.gwt.user.client.ui.HasValue#setValue(java.lang.Object)
	 */
	public void setValue(List<Label> value) {
		changes = new HashMap<Key, Label>();
		cellList.clearSelection();
		setFields();
		List<Label> labels =
			Managers.LABELS_MANAGER.getLabels(LabelType.USER_DEFINED);
		for (Label l : labels) {
			Shortcut cut =
				Managers.SHORTCUTS_MANAGER.getByReferenced(l.getKey());
			l.setShortcutStroke(cut != null ? cut.getKeyStroke() : null);
		}
		cellList.setRowData(labels);
	}

	/**
	 * Returns copy (may be an existing one) of {@link Label} used to store its
	 * changes.
	 * 
	 * @param label
	 *            {@link Label} to copy
	 * @return copy of {@link Label}
	 */
	private Label getLabel(Label label) {
		if (label == null || label.getKey() == null) {
			return null;
		}
		Label l = changes.get(label.getKey());
		if (l == null) {
			l = new Label(null, label.getName());
			l.setAppearance(new LabelAppearance(label.getAppearance()));
			l.setBackupLevel(label.getBackupLevel());
			l.setDisplayName(label.getDisplayName());
			l.setKey(label.getKey());
			l.setLabelType(label.getLabelType());
			l.setToBeDeleted(label.isToBeDeleted());
			l.setPriority(label.getPriority());
			l.setShortcutStroke(label.getShortcutStroke());
		}
		return l;
	}

	/**
	 * Sets fields in the dialog according to currently selected {@link Label}.
	 */
	private void setFields() {
		Label selected = getLabel(cellList.getSelected());
		if (selected == null) {
			name.setText("");
			foregroundColor.setEnabled(false);
			foregroundColor.setValue(null, false);
			backgroundColor.setEnabled(false);
			backgroundColor.setValue(null, false);
			markedToDelete.setEnabled(false);
			markedToDelete.setDown(false);
			backupLevel.setValue(BackupLevel.NO_BACKUP, false);
			backupLevel.setEnabled(false);
			shortcut.setEnabled(false);
			shortcut.setValue(null, false);
		} else {
			name.setText(selected.getDisplayName());
			foregroundColor.setEnabled(true);
			foregroundColor.setValue(selected
				.getAppearance()
				.getForegroundColor(), false);
			backgroundColor.setEnabled(true);
			backgroundColor.setValue(selected
				.getAppearance()
				.getBackgroundColor(), false);
			backupLevel.setValue(selected.getBackupLevel(), false);
			backupLevel.setEnabled(true);
			markedToDelete.setEnabled(true);
			markedToDelete.setDown(selected.isToBeDeleted());
			shortcut.setEnabled(true);
			shortcut.setValue(selected.getShortcutStroke(), false);
		}
	}

	public void setValue(List<Label> value, boolean fireEvents) {
		setValue(value);
	}
}
