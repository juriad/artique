package cz.artique.client.artiqueListFilters;

import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;

import cz.artique.shared.model.label.FilterOrder;

public class FilterOrderPicker extends Composite implements HasEnabled {
	private final FlowPanel panel;
	private final RadioButton descendingOrder;
	private final RadioButton ascendingOrder;
	private final Label label;

	private boolean enabled = true;

	@UiConstructor
	public FilterOrderPicker(String group) {
		panel = new FlowPanel();
		initWidget(panel);

		label = new Label();
		descendingOrder = new RadioButton(group);
		if (FilterOrder.DESCENDING.equals(FilterOrder.getDefault())) {
			descendingOrder.setValue(true);
		}
		panel.add(descendingOrder);
		ascendingOrder = new RadioButton(group);
		if (FilterOrder.ASCENDING.equals(FilterOrder.getDefault())) {
			ascendingOrder.setValue(true);
		}
		panel.add(ascendingOrder);
	}

	public void setMsg(FilterOrder option, String msg) {
		switch (option) {
		case ASCENDING:
			ascendingOrder.setText(msg);
			break;
		case DESCENDING:
			descendingOrder.setText(msg);
			break;
		default:
			break;
		}
	}

	public void setLabel(String text) {
		label.setText(text);
	}

	public void setValue(FilterOrder value) {
		switch (value) {
		case ASCENDING:
			ascendingOrder.setValue(true);
			break;
		case DESCENDING:
			descendingOrder.setValue(true);
			break;
		default:
			break;
		}
	}

	public FilterOrder getValue() {
		if (descendingOrder.getValue()) {
			return FilterOrder.DESCENDING;
		} else if (ascendingOrder.getValue()) {
			return FilterOrder.ASCENDING;
		} else {
			// should not happen
			return FilterOrder.getDefault();
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;

		ascendingOrder.setEnabled(enabled);
		descendingOrder.setEnabled(enabled);
	}
}
