package cz.artique.client.artiqueListFilters;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

import cz.artique.shared.model.label.ListFilter;

public class FilterEditor extends Composite
		implements HasEnabled, HasValue<ListFilter> {

	private static FilterEditorUiBinder uiBinder = GWT
		.create(FilterEditorUiBinder.class);

	interface FilterEditorUiBinder extends UiBinder<Widget, FilterEditor> {}

	@UiField
	ArtiqueQueryFilter filter;

	@UiField
	OptionalValue<DateBox, Date> startFrom;

	@UiField
	OptionalDateBox endTo;

	@UiField
	TriStatePicker readPicker;

	@UiField
	FilterOrderPicker orderPicker;

	private boolean enabled = true;

	public FilterEditor() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;

		// FIXME tady dokončit
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<ListFilter> handler) {
		// TODO Auto-generated method stub
		return null;
	}

	public ListFilter getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setValue(ListFilter value) {
		// TODO Auto-generated method stub

	}

	public void setValue(ListFilter value, boolean fireEvents) {
		// TODO Auto-generated method stub

	}

}
