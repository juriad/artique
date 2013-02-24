package cz.artique.client.artiqueLabels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.InlineLabel;

import cz.artique.client.artiqueHistory.ArtiqueHistory;
import cz.artique.client.artiqueHistory.CachingHistoryUtils;
import cz.artique.client.labels.LabelWidget;
import cz.artique.client.labels.LabelWidgetFactory;
import cz.artique.client.labels.RemoveEvent;
import cz.artique.client.labels.RemoveHandler;
import cz.artique.client.utils.InlineFlowPanel;
import cz.artique.shared.model.label.Filter;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.ListFilter;

public class ArtiqueLabelWidget extends Composite
		implements LabelWidget<Label>, HasEnabled {

	public static final ArtiqueLabelWidgetFactory FACTORY =
		new ArtiqueLabelWidgetFactory(false);

	public static final ArtiqueLabelWidgetFactory REMOVABLE_FACTORY =
		new ArtiqueLabelWidgetFactory(true);

	public static class ArtiqueLabelWidgetFactory
			implements LabelWidgetFactory<Label> {

		private final boolean removable;

		public ArtiqueLabelWidgetFactory(boolean removable) {
			this.removable = removable;
		}

		public ArtiqueLabelWidget createWidget(Label l) {
			return new ArtiqueLabelWidget(l, removable);
		}
	}

	private final static String removeSign = "x";

	private final Label label;
	private boolean enabled = true;
	private InlineFlowPanel panel;

	private InlineLabel removeButton;

	private final boolean removable;

	public ArtiqueLabelWidget(final Label label, boolean removable) {
		this.label = label;
		this.removable = removable;
		panel = new InlineFlowPanel();
		initWidget(panel);
		setStylePrimaryName("label");
		Filter filter = CachingHistoryUtils.UTILS.getFilterForLabel(label);
		String serialized =
			CachingHistoryUtils.UTILS.serializeListFilter(filter);
		Anchor nameLabel = new Anchor(label.getName(), "#" + serialized);

		panel.add(nameLabel);
		nameLabel.setStylePrimaryName("label-name");
		nameLabel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (isEnabled()) {
					Filter filter =
						CachingHistoryUtils.UTILS.getFilterForLabel(label);
					ListFilter listFilter =
						ArtiqueHistory.HISTORY.getBaseListFilter();
					listFilter.setFilterObject(filter);
					String serialized =
						CachingHistoryUtils.UTILS.serializeListFilter(filter);
					ArtiqueHistory.HISTORY
						.addListFilter(listFilter, serialized);
				}
				event.preventDefault();
			}
		});

		if (isRemovable()) {
			removeButton = new InlineLabel(removeSign);
			removeButton.setStylePrimaryName("label-remove-button");
			panel.add(removeButton);
			removeButton.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					fireEvent(new RemoveEvent());
				}
			});
		}
	}

	public HandlerRegistration addRemoveHandler(RemoveHandler handler) {
		if (!isRemovable()) {
			throw new IllegalStateException("This label is not removable");
		}
		return addHandler(handler, RemoveEvent.getType());
	}

	public Label getLabel() {
		return label;
	}

	public int compareTo(LabelWidget<Label> o) {
		return getLabel().compareTo(o.getLabel());
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isRemovable() {
		return removable;
	}
}
