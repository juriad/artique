package cz.artique.client.artiqueSources;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasValue;

import cz.artique.shared.model.source.UserSource;

public class SourceRegionPicker extends Composite
		implements HasEnabled, HasValue<UserSource> {

	private boolean enabled;
	private UserSource userSource;

	public SourceRegionPicker() {
		initWidget(new FlowPanel()); // XXX quick fix
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<UserSource> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	public UserSource getValue() {
		return userSource;
	}

	public void setValue(UserSource value) {
		this.userSource = value;
	}

	public void setValue(UserSource value, boolean fireEvents) {
		setValue(value);
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
