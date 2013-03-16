package cz.artique.client.artiqueListFilters;

import com.google.gwt.uibinder.client.UiConstructor;

public class ReadStatePicker extends EnumRadioPicker<ReadState> {

	@UiConstructor
	public ReadStatePicker() {
		super(ReadState.READ, "readState");
	}
}
