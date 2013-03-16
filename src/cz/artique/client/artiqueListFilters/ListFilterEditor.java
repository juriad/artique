package cz.artique.client.artiqueListFilters;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

import cz.artique.shared.model.label.ListFilter;

public class ListFilterEditor extends Composite
		implements HasEnabled, HasValue<ListFilter> {

	private static ListFilterEditorUiBinder uiBinder = GWT
		.create(ListFilterEditorUiBinder.class);

	interface ListFilterEditorUiBinder
			extends UiBinder<Widget, ListFilterEditor> {}

	@UiField
	Grid grid;

	@UiField
	ArtiqueQueryFilter filter;

	@UiField
	OptionalValue<DateBox, Date> startFrom;

	@UiField
	OptionalValue<DateBox, Date> endTo;

	@UiField
	OptionalValue<ReadStatePicker, ReadState> readPicker;

	@UiField
	ListFilterOrderPicker orderPicker;

	private boolean enabled = true;

	public ListFilterEditor() {
		initWidget(uiBinder.createAndBindUi(this));
		Element element = grid.getCellFormatter().getElement(0, 0);
		DOM.setElementAttribute(element, "colspan", "3");
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		filter.setEnabled(enabled);
		startFrom.setEnabled(enabled);
		endTo.setEnabled(enabled);
		readPicker.setEnabled(enabled);
		orderPicker.setEnabled(enabled);
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<ListFilter> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	public ListFilter getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setValue(ListFilter value) {
		filter.setFilter(value.getFilterObject());
		startFrom.setValue(value.getStartFrom());
		endTo.setValue(value.getEndTo());

		Boolean read = value.getRead();
		readPicker.setValue(ReadState.get(read));
		orderPicker.setValue(value.getOrder());
	}

	public void setValue(ListFilter value, boolean fireEvents) {
		setValue(value);
	}

}
