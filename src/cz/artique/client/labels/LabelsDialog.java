package cz.artique.client.labels;

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
