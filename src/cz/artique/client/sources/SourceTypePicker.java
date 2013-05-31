package cz.artique.client.sources;

import com.google.gwt.uibinder.client.UiConstructor;

import cz.artique.client.common.EnumRadioPicker;
import cz.artique.client.i18n.I18n;
import cz.artique.shared.model.source.SourceType;

public class SourceTypePicker extends EnumRadioPicker<SourceType> {

	@UiConstructor
	public SourceTypePicker() {
		super(SourceType.RSS_ATOM, "sourceType", I18n.getSourcesConstants());
	}

	@Override
	protected boolean canBeSelected(SourceType enu) {
		if (SourceType.MANUAL.equals(enu)) {
			return false;
		}
		return super.canBeSelected(enu);
	}
}
