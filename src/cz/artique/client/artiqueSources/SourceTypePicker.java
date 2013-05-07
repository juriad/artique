package cz.artique.client.artiqueSources;

import com.google.gwt.uibinder.client.UiConstructor;

import cz.artique.client.artiqueListFilters.EnumRadioPicker;

public class SourceTypePicker extends EnumRadioPicker<SourceType> {

	@UiConstructor
	public SourceTypePicker() {
		super(SourceType.RSS_ATOM, "sourceType");
	}

	@Override
	protected boolean canBeSelected(SourceType enu) {
		if (SourceType.MANUAL.equals(enu)) {
			return false;
		}
		return super.canBeSelected(enu);
	}
}
