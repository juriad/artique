package cz.artique.client.listFilters;

import com.google.gwt.uibinder.client.UiConstructor;

import cz.artique.client.common.EnumRadioPicker;

public class ReadStatePicker extends EnumRadioPicker<ReadState> {

	@UiConstructor
	public ReadStatePicker() {
		super(ReadState.READ, "readState");
	}
}
