package cz.artique.client.artiqueSources;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasValue;

import cz.artique.shared.model.source.HasRegion;

public class SourceRegionPicker extends Composite
		implements HasEnabled, HasValue<HasRegion> {

	private boolean enabled;
	private HasRegion hasRegion;

	public SourceRegionPicker() {
		initWidget(new FlowPanel()); // XXX quick fix
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<HasRegion> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	/**
	 * Called on source save
	 */
	public void saveRegion() {
		if (hasRegion == null) {
			return;
		}

		// TODO save
	}

	public HasRegion getValue() {
		return hasRegion;
	}

	public void setValue(HasRegion value) {
		hasRegion = value;
	}

	public void setValue(HasRegion value, boolean fireEvents) {
		setValue(value);
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
