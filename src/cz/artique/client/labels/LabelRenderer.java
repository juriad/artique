package cz.artique.client.labels;

import com.google.gwt.text.shared.AbstractRenderer;

import cz.artique.shared.model.label.Label;

public class LabelRenderer extends AbstractRenderer<Label> {

	private final boolean descriptive;

	public LabelRenderer(boolean descriptive) {
		this.descriptive = descriptive;

	}

	public String render(Label object) {
		switch (object.getLabelType()) {
		case SYSTEM:
			return descriptive ? object.getDisplayName() : object.getName();
		case USER_DEFINED:
			return descriptive ? object.getDisplayName() : object.getName();
		case USER_SOURCE:
			return object.getDisplayName();
		default:
			return "Unknown label type: " + object.getLabelType()
				+ " for label: " + object.getName();
		}
	}

	public boolean isDescriptive() {
		return descriptive;
	}

}
