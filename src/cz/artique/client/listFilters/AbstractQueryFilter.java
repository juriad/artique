package cz.artique.client.listFilters;

import java.util.List;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import cz.artique.client.common.AddButton;
import cz.artique.client.labels.LabelWidget;
import cz.artique.client.labels.suggestion.LabelSuggestion;
import cz.artique.client.labels.suggestion.LabelsPool;
import cz.artique.client.labels.suggestion.SuggestionResult;
import cz.artique.shared.model.label.Filter;
import cz.artique.shared.model.label.Label;

public abstract class AbstractQueryFilter extends Composite {

	class LabelCloseHandler implements CloseHandler<LabelWidget> {
		public void onClose(CloseEvent<LabelWidget> e) {
			LabelWidget source = e.getTarget();

			int widgetIndex = panel.getWidgetIndex(source);
			if (widgetIndex >= 0) {
				// add button
				panel.remove(widgetIndex + 1);
				panel.remove(widgetIndex);
				labels.remove(source.getLabel());
			}
		}
	}

	class AddOpenHandler implements OpenHandler<AbstractQueryFilter> {
		private final LabelSuggestion labelSuggestion;
		private HandlerRegistration selectionHandler;

		public AddOpenHandler() {
			labelSuggestion = new LabelSuggestion(pool, 20);
		}

		public void onOpen(OpenEvent<AbstractQueryFilter> event) {
			final Widget source = (Widget) event.getSource();
			final int widgetIndex = panel.getWidgetIndex(source);
			if (widgetIndex < 0) {
				return;
			}

			source.setVisible(false);
			panel.insert(labelSuggestion, widgetIndex);
			selectionHandler =
				labelSuggestion
					.addSelectionHandler(new SelectionHandler<SuggestionResult>() {
						public void onSelection(
								SelectionEvent<SuggestionResult> event) {
							labelSuggestion.removeFromParent();
							removeHandler();

							source.setVisible(true);

							Label added =
								getAddedLabel(event.getSelectedItem());

							if (added != null) {
								LabelWidget labelWidget = createWidget(added);
								labelWidget.addCloseHandler(closeHandler);
								panel.insert(labelWidget, widgetIndex);
								AddButton<AbstractQueryFilter> addButton =
									AddButton.FACTORY
										.createWidget(AbstractQueryFilter.this);
								addButton.addOpenHandler(openHandler);
								panel.insert(addButton, widgetIndex);

								labels.add(widgetIndex / 2, added);
							} else {
								// ignore non existing label
							}
						}
					});
			labelSuggestion.focus();
		}

		private void removeHandler() {
			selectionHandler.removeHandler();
		}
	}

	private final FlowPanel panel;

	private List<Label> labels;

	private final LabelCloseHandler closeHandler;

	private final AddOpenHandler openHandler;

	private final LabelsPool pool;

	public AbstractQueryFilter(LabelsPool pool) {
		this.pool = pool;
		closeHandler = new LabelCloseHandler();
		openHandler = new AddOpenHandler();
		panel = new FlowPanel();
		initWidget(panel);
		setStylePrimaryName("queryFilter");
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
			// first
			AddButton<AbstractQueryFilter> addButton =
				AddButton.FACTORY.createWidget(this);
			addButton.addOpenHandler(openHandler);
			panel.add(addButton);
		}
		for (Label l : labels2) {
			LabelWidget labelWidget = createWidget(l);
			labelWidget.addCloseHandler(closeHandler);
			panel.add(labelWidget);
			// after each
			AddButton<AbstractQueryFilter> addButton =
				AddButton.FACTORY.createWidget(this);
			addButton.addOpenHandler(openHandler);
			panel.add(addButton);
		}
	}

	protected abstract LabelWidget createWidget(Label l);

	protected abstract List<Label> getLabelsFromFilter(Filter filter);

	protected abstract Label getAddedLabel(SuggestionResult selectedItem);

	protected List<Label> getLabels() {
		return labels;
	}

	public abstract Filter getFilter();
}
