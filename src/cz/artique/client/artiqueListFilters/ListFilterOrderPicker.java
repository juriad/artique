package cz.artique.client.artiqueListFilters;

import com.google.gwt.uibinder.client.UiConstructor;

import cz.artique.shared.model.label.ListFilterOrder;

public class ListFilterOrderPicker extends EnumRadioPicker<ListFilterOrder> {

	@UiConstructor
	public ListFilterOrderPicker() {
		super(ListFilterOrder.getDefault(), "listFilterOrder");
	}
}
