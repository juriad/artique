package cz.artique.client.artiqueListFilters;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;

public class OptionalValue<E extends HasValue<F> & IsWidget & HasEnabled, F>
		extends Composite implements HasEnabled, HasValue<F> {

	private final FlowPanel flowPanel;
	private E e;
	private final CheckBox checkBox;

	private boolean enabled = true;

	public OptionalValue() {
		flowPanel = new FlowPanel();
		initWidget(flowPanel);
		checkBox = new CheckBox();
		flowPanel.add(checkBox);

		checkBox.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				e.setEnabled(checkBox.getValue());
				ValueChangeEvent.fire(OptionalValue.this, getValue());
			}
		});
	}

	@UiChild(tagname = "value")
	protected void setE(E e) {
		this.e = e;
		flowPanel.add(e);
		setValue(null, false);
	}

	public void setLabel(String label) {
		checkBox.setText(label);
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		checkBox.setEnabled(enabled);
		e.setEnabled(enabled);
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<F> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	public F getValue() {
		if (e.isEnabled()) {
			return e.getValue();
		}
		return null;
	}

	public void setValue(F value) {
		setValue(value, true);
	}

	public void setValue(F value, boolean fireEvents) {
		checkBox.setValue(value != null);
		e.setEnabled(value != null);
		e.setValue(value);
		if (fireEvents) {
			ValueChangeEvent.fire(this, getValue());
		}
	}
}
