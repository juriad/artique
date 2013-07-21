package cz.artique.client.listFilters;

import com.google.gwt.uibinder.client.UiConstructor;

import cz.artique.client.common.EnumRadioPicker;
import cz.artique.client.i18n.I18n;

public class ReadStatePicker extends EnumRadioPicker<ReadState> {

	@UiConstructor
	public ReadStatePicker() {
		super(ReadState.ALL, "readState", I18n.getListFiltersConstants());
	}
}
