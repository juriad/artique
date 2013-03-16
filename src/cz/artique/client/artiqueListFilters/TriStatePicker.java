package cz.artique.client.artiqueListFilters;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;

public class TriStatePicker extends Composite
		implements HasEnabled, HasValue<Boolean> {

	private final FlowPanel panel;
	private final RadioButton nullOption;
	private final RadioButton trueOption;
	private final RadioButton falseOption;
	private final Label label;

	private boolean enabled = true;

	@UiConstructor
	public TriStatePicker(String group) {
		panel = new FlowPanel();
		initWidget(panel);

		label = new Label();
		nullOption = new RadioButton(group);
		nullOption.setText("null");
		nullOption.setValue(true);
		panel.add(nullOption);
		trueOption = new RadioButton(group);
		panel.add(trueOption);
		falseOption = new RadioButton(group);
		panel.add(falseOption);
	}

	public void setMsg(Boolean option, String msg) {
		if (option == null) {
			nullOption.setText(msg);
		} else if (option) {
			trueOption.setText(msg);
		} else {
			falseOption.setText(msg);
		}
	}

	public void setLabel(String text) {
		label.setText(text);
	}

	public void setValue(Boolean value) {
		setValue(value, true);
	}

	public Boolean getValue() {
		if (trueOption.getValue()) {
			return true;
		} else if (falseOption.getValue()) {
			return false;
		} else {
			return null;
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;

		nullOption.setEnabled(enabled);
		trueOption.setEnabled(enabled);
		falseOption.setEnabled(enabled);
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Boolean> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	public void setValue(Boolean value, boolean fireEvents) {
		if (value == null) {
			nullOption.setValue(true);
		} else if (value) {
			trueOption.setValue(true);
		} else {
			falseOption.setValue(true);
		}

		if (fireEvents) {
			ValueChangeEvent.fire(this, getValue());
		}
	}

}
