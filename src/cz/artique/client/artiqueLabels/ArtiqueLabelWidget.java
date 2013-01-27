package cz.artique.client.artiqueLabels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;

import cz.artique.client.labels.LabelWidget;
import cz.artique.client.labels.LabelWidgetFactory;
import cz.artique.client.labels.RemoveEvent;
import cz.artique.client.labels.RemoveHandler;
import cz.artique.client.utils.InlineFlowPanel;
import cz.artique.shared.model.label.Label;

public class ArtiqueLabelWidget extends Composite
		implements LabelWidget<Label>, HasGeneralClickHandlers {

	public static final ArtiqueLabelWidgetFactory factory =
		new ArtiqueLabelWidgetFactory();

	public static class ArtiqueLabelWidgetFactory
			implements LabelWidgetFactory<Label> {

		public ArtiqueLabelWidget createWidget(Label l) {
			return new ArtiqueLabelWidget(l);
		}
	}

	private final static String removeSign = "x";

	private final Label label;
	private boolean enabled = true;
	private InlineFlowPanel panel;

	private final InlineLabel removeButton;

	public ArtiqueLabelWidget(Label label) {
		this.label = label;
		panel = new InlineFlowPanel();
		initWidget(panel);
		setStylePrimaryName("label");
		InlineLabel nameLabel = new InlineLabel(label.getName());
		panel.add(nameLabel);
		nameLabel.setStylePrimaryName("label-name");
		nameLabel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				fireEvent(new GeneralClickEvent());
			}
		});

		removeButton = new InlineLabel(removeSign);
		removeButton.setStylePrimaryName("label-remove-button");
		panel.add(removeButton);
		removeButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				fireEvent(new RemoveEvent());
			}
		});
	}

	public HandlerRegistration addRemoveHandler(RemoveHandler handler) {
		return addHandler(handler, RemoveEvent.getType());
	}

	public Label getLabel() {
		return label;
	}

	public HandlerRegistration addGeneralClickHandler(
			GeneralClickHandler handler) {
		return addHandler(handler, GeneralClickEvent.getType());
	}

	public int compareTo(LabelWidget<Label> o) {
		return getLabel().compareTo(o.getLabel());
	}

	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Affects only removeButton. GeneralClicks are generated normally
	 * 
	 * @see com.google.gwt.user.client.ui.HasEnabled#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;

		removeButton.setVisible(enabled);
	}
}
