package cz.artique.client.artiqueListFilters;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.datepicker.client.DateBox;

public class OptionalDateBox extends Composite
		implements HasEnabled, HasValue<Date> {

	private final FlowPanel panel;
	private final DateBox dateBox;
	private final CheckBox checkBox;

	private boolean enabled = true;

	public OptionalDateBox() {
		panel = new FlowPanel();
		initWidget(panel);
		checkBox = new CheckBox();
		panel.add(checkBox);

		dateBox = new DateBox();
		panel.add(dateBox);

		setValue(null, false);

		checkBox.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dateBox.setEnabled(checkBox.getValue());
				ValueChangeEvent.fire(OptionalDateBox.this, getValue());
			}
		});
	}

	public void setLabel(String msg) {
		checkBox.setText(msg);
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;

		checkBox.setEnabled(enabled);
		dateBox.setEnabled(enabled);
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Date> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	public Date getValue() {
		if (dateBox.isEnabled()) {
			return dateBox.getValue();
		}
		return null;
	}

	public void setValue(Date value) {
		setValue(value, true);
	}

	public void setValue(Date value, boolean fireEvents) {
		checkBox.setValue(value != null);
		dateBox.setEnabled(value != null);
		dateBox.setValue(value);
		if (fireEvents) {
			ValueChangeEvent.fire(this, getValue());
		}
	}
}
