package cz.artique.client.artiqueListFilters;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.Widget;

import cz.artique.client.labels.AddLabelButtonFactory;
import cz.artique.client.labels.LabelWidget;
import cz.artique.client.labels.LabelWidgetFactory;
import cz.artique.client.labels.LabelsManager;
import cz.artique.client.labels.RemoveEvent;
import cz.artique.client.labels.RemoveHandler;
import cz.artique.client.labels.suggestion.LabelSuggestion;
import cz.artique.client.labels.suggestion.SuggesionLabelFactory;
import cz.artique.client.labels.suggestion.SuggestionResult;
import cz.artique.shared.model.label.Filter;
import cz.artique.shared.model.label.Label;

public abstract class AbstractQueryFilter extends Composite
		implements HasEnabled {

	class LabelRemoveHandler implements RemoveHandler {

		public void onRemove(RemoveEvent e) {
			@SuppressWarnings("unchecked")
			LabelWidget<Label> source = (LabelWidget<Label>) e.getSource();

			int widgetIndex = panel.getWidgetIndex(source);
			if (widgetIndex >= 0) {
				// add button
				panel.remove(widgetIndex + 1);
				panel.remove(widgetIndex);
				labels.remove(source.getLabel());
			}
		}
	}

	class AddClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			final Widget source = (Widget) event.getSource();
			final int widgetIndex = panel.getWidgetIndex(source);
			if (widgetIndex < 0) {
				return;
			}

			final LabelSuggestion<Label> labelSuggestion =
				new LabelSuggestion<Label>(manager, manager.getLabels(null),
					factory2, false);
			source.setVisible(false);
			panel.insert(labelSuggestion, widgetIndex);
			labelSuggestion
				.addSelectionHandler(new SelectionHandler<SuggestionResult<Label>>() {

					public void onSelection(
							SelectionEvent<SuggestionResult<Label>> event) {
						labelSuggestion.removeFromParent();
						source.setVisible(true);

						Label added = getAddedLabel(event.getSelectedItem());

						if (added != null) {
							LabelWidget<Label> labelWidget =
								factory.createWidget(added);
							labelWidget.addRemoveHandler(removeHandler);
							panel.insert(labelWidget, widgetIndex);
							com.google.gwt.user.client.ui.Label addButton =
								AddLabelButtonFactory.FACTORY.createAddLabel();
							addButton.addClickHandler(addHandler);
							panel.insert(addButton, widgetIndex);

							labels.add(widgetIndex / 2, added);
						} else {
							// TODO vynadat uzivateli
						}
					}
				});
			labelSuggestion.focus();
		}
	}

	private FlowPanel panel;

	private List<Label> labels;

	private LabelWidgetFactory<Label> factory;

	private SuggesionLabelFactory<Label> factory2;

	private boolean enabled = true;

	private final LabelRemoveHandler removeHandler = new LabelRemoveHandler();

	private final AddClickHandler addHandler = new AddClickHandler();

	protected final LabelsManager<Label, ?> manager;

	public AbstractQueryFilter(LabelsManager<Label, ?> manager,
			LabelWidgetFactory<Label> factory,
			SuggesionLabelFactory<Label> factory2) {
		this.manager = manager;
		this.factory = factory;
		this.factory2 = factory2;
		panel = new FlowPanel();
		initWidget(panel);
		setFilter(new Filter());
	}

	public void setFilter(Filter filter) {
		if (filter == null) {
			filter = new Filter();
		}

		labels = getLabelsFromFilter(filter);
		fillPanel(labels);
	}

	private void fillPanel(List<Label> labels2) {
		panel.clear();
		{
			com.google.gwt.user.client.ui.Label addButton =
				AddLabelButtonFactory.FACTORY.createAddLabel();
			addButton.addClickHandler(addHandler);
			panel.add(addButton);
		}
		for (Label l : labels2) {
			LabelWidget<Label> labelWidget = factory.createWidget(l);
			labelWidget.addRemoveHandler(removeHandler);
			panel.add(labelWidget);
			com.google.gwt.user.client.ui.Label addButton =
				AddLabelButtonFactory.FACTORY.createAddLabel();
			addButton.addClickHandler(addHandler);
			panel.add(addButton);
		}
	}

	protected abstract List<Label> getLabelsFromFilter(Filter filter);

	protected abstract Label getAddedLabel(SuggestionResult<Label> selectedItem);

	protected List<Label> getLabels() {
		return labels;
	}

	public abstract Filter getFilter();

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
