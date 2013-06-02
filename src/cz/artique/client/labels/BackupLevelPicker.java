package cz.artique.client.labels;

import com.google.gwt.uibinder.client.UiConstructor;

import cz.artique.client.common.EnumRadioPicker;
import cz.artique.client.i18n.I18n;
import cz.artique.shared.model.label.BackupLevel;

public class BackupLevelPicker extends EnumRadioPicker<BackupLevel> {

	@UiConstructor
	public BackupLevelPicker() {
		super(BackupLevel.NO_BACKUP, "backupLevel", I18n.getLabelsConstants());
	}
}
