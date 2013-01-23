package cz.artique.client.artiqueFilter;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.Widget;

import cz.artique.client.labels.LabelWidget;
import cz.artique.client.labels.LabelWidgetFactory;
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
				new LabelSuggestion<Label>(getLabelsToSuggest(), factory2);
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
								newAddButton();
							addButton.addClickHandler(addHandler);
							panel.insert(addButton, widgetIndex + 1);
						} else {
							// TODO vynadat uzivateli
						}
					}
				});
			labelSuggestion.focus();
		}
	}

	private FlowPanel panel;

	protected static final String addLabelSign = "+";

	private List<Label> labels;

	private LabelWidgetFactory<Label> factory;

	private SuggesionLabelFactory<Label> factory2;

	private boolean enabled = true;

	private final LabelRemoveHandler removeHandler = new LabelRemoveHandler();

	private final AddClickHandler addHandler = new AddClickHandler();

	public AbstractQueryFilter(LabelWidgetFactory<Label> factory,
			SuggesionLabelFactory<Label> factory2, Filter filter) {
		this.factory = factory;
		this.factory2 = factory2;
		panel = new FlowPanel();
		initWidget(panel);

		labels = getLabelsFromFilter(filter);

		fillPanel(labels);
	}

	private void fillPanel(List<Label> labels2) {
		{
			com.google.gwt.user.client.ui.Label addButton = newAddButton();
			addButton.addClickHandler(addHandler);
			panel.add(addButton);
		}
		for (Label l : labels2) {
			LabelWidget<Label> labelWidget = factory.createWidget(l);
			labelWidget.addRemoveHandler(removeHandler);
			panel.add(labelWidget);
			com.google.gwt.user.client.ui.Label addButton = newAddButton();
			addButton.addClickHandler(addHandler);
			panel.add(addButton);
		}
	}

	protected abstract List<Label> getLabelsFromFilter(Filter filter);

	protected abstract com.google.gwt.user.client.ui.Label newAddButton();

	protected abstract List<Label> getLabelsToSuggest();

	protected abstract Label getAddedLabel(SuggestionResult<Label> selectedItem);

	protected List<Label> getLabels() {
		return labels;
	}

	public abstract Filter getFilter();

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		if (enabled == this.enabled) {
			return;
		}
		this.enabled = enabled;

		for (int i = 0; i < panel.getWidgetCount(); i++) {
			Widget widget = panel.getWidget(i);
			if (widget instanceof com.google.gwt.user.client.ui.Label) {
				widget.setVisible(enabled);
			} else if (widget instanceof LabelWidget) {
				@SuppressWarnings("unchecked")
				LabelWidget<Label> labelWidget = ((LabelWidget<Label>) widget);
				labelWidget.setEnabled(enabled);
			}
		}
	}
}
