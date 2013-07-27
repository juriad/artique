package cz.artique.client.labels;

import com.google.gwt.text.shared.AbstractRenderer;

import cz.artique.shared.model.label.Label;

/**
 * Renders label name in two manners; the difference is at system labels.
 * 
 * @author Adam Juraszek
 * 
 */
public class LabelRenderer extends AbstractRenderer<Label> {

	private final boolean descriptive;

	/**
	 * @param descriptive
	 *            whether the system label shall be rendered with description
	 */
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

	/**
	 * How to render system labels.
	 * 
	 * @return whether the system label shall be rendered with description
	 */
	public boolean isDescriptive() {
		return descriptive;
	}

}
