package cz.artique.client.artiqueFilter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.Widget;

import cz.artique.shared.model.label.ListFilter;

public class FilterEditor extends Composite implements HasEnabled {

	private static FilterEditorUiBinder uiBinder = GWT
		.create(FilterEditorUiBinder.class);

	interface FilterEditorUiBinder extends UiBinder<Widget, FilterEditor> {}

	@UiField
	ArtiqueQueryFilter filter;

	@UiField
	OptionalDateBox startFrom;

	@UiField
	OptionalDateBox endTo;

	@UiField
	TriStatePicker readPicker;

	@UiField
	FilterOrderPicker orderPicker;

	private boolean enabled = true;

	public FilterEditor(ListFilter listFilter) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;

		// FIXME tady dokončit
	}

}
