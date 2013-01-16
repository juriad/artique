package cz.artique.client.artiqueLabels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ComplexPanel;

import cz.artique.client.labels.LabelWidget;
import cz.artique.client.labels.LabelWidgetFactory;
import cz.artique.client.labels.RemoveEvent;
import cz.artique.client.labels.RemoveHandler;
import cz.artique.shared.model.label.Label;

public class ArtiqueLabelWidget extends ComplexPanel
		implements LabelWidget<Label>, HasGeneralClickHandlers {

	public static final ArtiqueLabelWidgetFactory factory =
		new ArtiqueLabelWidgetFactory();

	public static class ArtiqueLabelWidgetFactory
			implements LabelWidgetFactory<Label> {
		public ArtiqueLabelWidget createWidget(Label l) {
			return new ArtiqueLabelWidget(l, false);
		}
	}

	private final static String removeSign = "x";

	private final Label label;
	private final boolean readOnly;

	public ArtiqueLabelWidget(Label label, boolean readOnly) {
		this.label = label;
		this.readOnly = readOnly;
		com.google.gwt.user.client.ui.Label nameLabel =
			new com.google.gwt.user.client.ui.Label(label.getName());
		add(nameLabel);
		nameLabel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				fireEvent(new GeneralClickEvent());
			}
		});

		if (!isReadOnly()) {
			com.google.gwt.user.client.ui.Label remove =
				new com.google.gwt.user.client.ui.Label(removeSign);
			add(remove);
			remove.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					fireEvent(new RemoveEvent());
				}
			});
		}
	}

	public HandlerRegistration addRemoveHandler(RemoveHandler handler) {
		return addHandler(handler, RemoveEvent.getType());
	}

	public Label getLabel() {
		return label;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public HandlerRegistration addGeneralClickHandler(
			GeneralClickHandler handler) {
		return addHandler(handler, GeneralClickEvent.getType());
	}

	public int compareTo(LabelWidget<Label> o) {
		return ArtiqueLabelsManager.getComparator().compare(getLabel(),
			o.getLabel());
	}
}