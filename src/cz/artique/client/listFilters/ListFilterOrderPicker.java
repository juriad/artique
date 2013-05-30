package cz.artique.client.listFilters;

import com.google.gwt.uibinder.client.UiConstructor;

import cz.artique.client.common.EnumRadioPicker;
import cz.artique.shared.model.label.ListFilterOrder;

public class ListFilterOrderPicker extends EnumRadioPicker<ListFilterOrder> {

	@UiConstructor
	public ListFilterOrderPicker() {
		super(ListFilterOrder.getDefault(), "listFilterOrder");
	}
}
