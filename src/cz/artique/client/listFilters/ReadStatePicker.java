package cz.artique.client.listFilters;

import com.google.gwt.uibinder.client.UiConstructor;

import cz.artique.client.common.EnumRadioPicker;
import cz.artique.client.i18n.I18n;

/**
 * Allows user to select read state criterion value as a set of radio buttons.
 * 
 * @author Adam Juraszek
 * 
 */
public class ReadStatePicker extends EnumRadioPicker<ReadState> {

	@UiConstructor
	public ReadStatePicker() {
		super(ReadState.ALL, "readState", I18n.getListFiltersConstants());
	}
}
