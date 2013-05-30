package cz.artique.client.artiqueListFilters;

import java.util.List;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.Widget;

import cz.artique.client.common.AddButton;
import cz.artique.client.labels.LabelWidget;
import cz.artique.client.labels.LabelWidgetFactory;
import cz.artique.client.labels.suggestion.LabelSuggestion;
import cz.artique.client.labels.suggestion.SuggestionResult;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.label.Filter;
import cz.artique.shared.model.label.Label;

public abstract class AbstractQueryFilter extends Composite
		implements HasEnabled {

	class LabelCloseHandler implements CloseHandler<LabelWidget> {

		public void onClose(CloseEvent<LabelWidget> e) {
			LabelWidget source = (LabelWidget) e.getTarget();

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
		public void onOpen(OpenEvent<AbstractQueryFilter> event) {
			final Widget source = (Widget) event.getSource();
			final int widgetIndex = panel.getWidgetIndex(source);
			if (widgetIndex < 0) {
				return;
			}

			final LabelSuggestion labelSuggestion =
				new LabelSuggestion(Managers.LABELS_MANAGER.getLabels(null),
					false);
			source.setVisible(false);
			panel.insert(labelSuggestion, widgetIndex);
			labelSuggestion
				.addSelectionHandler(new SelectionHandler<SuggestionResult>() {

					public void onSelection(
							SelectionEvent<SuggestionResult> event) {
						labelSuggestion.removeFromParent();
						source.setVisible(true);

						Label added = getAddedLabel(event.getSelectedItem());

						if (added != null) {
							LabelWidget labelWidget =
								factory.createWidget(added);
							labelWidget.addCloseHandler(closeHandler);
							panel.insert(labelWidget, widgetIndex);
							AddButton<AbstractQueryFilter> addButton =
								AddButton.FACTORY
									.createWidget(AbstractQueryFilter.this);
							addButton.addOpenHandler(openHandler);
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

	private LabelWidgetFactory factory;

	private boolean enabled = true;

	private final LabelCloseHandler closeHandler = new LabelCloseHandler();

	private final AddOpenHandler openHandler = new AddOpenHandler();

	public AbstractQueryFilter(LabelWidgetFactory factory) {
		this.factory = factory;
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
			AddButton<AbstractQueryFilter> addButton =
				AddButton.FACTORY.createWidget(this);
			addButton.addOpenHandler(openHandler);
			panel.add(addButton);
		}
		for (Label l : labels2) {
			LabelWidget labelWidget = factory.createWidget(l);
			labelWidget.addCloseHandler(closeHandler);
			panel.add(labelWidget);
			AddButton<AbstractQueryFilter> addButton =
				AddButton.FACTORY.createWidget(this);
			addButton.addOpenHandler(openHandler);
			panel.add(addButton);
		}
	}

	protected abstract List<Label> getLabelsFromFilter(Filter filter);

	protected abstract Label getAddedLabel(SuggestionResult selectedItem);

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
