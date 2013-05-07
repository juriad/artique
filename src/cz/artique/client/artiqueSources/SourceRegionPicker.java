package cz.artique.client.artiqueSources;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasValue;

import cz.artique.shared.model.source.Region;

public class SourceRegionPicker extends Composite
		implements HasEnabled, HasValue<Region> {

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Region> handler) {
		// TODO Auto-generated method stub
		return null;
	}

	public Region getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setValue(Region value) {
		// TODO Auto-generated method stub

	}

	public void setValue(Region value, boolean fireEvents) {
		// TODO Auto-generated method stub

	}

	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setEnabled(boolean enabled) {
		// TODO Auto-generated method stub

	}

}
