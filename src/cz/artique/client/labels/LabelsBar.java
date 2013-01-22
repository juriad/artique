package cz.artique.client.labels;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

import cz.artique.client.labels.suggestion.LabelSuggestion;
import cz.artique.client.labels.suggestion.SuggesionLabelFactory;
import cz.artique.client.labels.suggestion.SuggestionResult;
import cz.artique.shared.utils.HasKey;
import cz.artique.shared.utils.HasName;

public abstract class LabelsBar<E extends HasName & HasKey<K> & Comparable<E>, K>
		extends Composite implements RemoveHandler {
	private static final String addLabelSign = "+";

	private Label addLabel;

	private List<E> selectedLabels;

	private final PanelWithMore<LabelWidget<E>> panel;

	protected final LabelsManager<E, K> manager;

	private LabelWidgetFactory<E> factory;

	public LabelsBar(final LabelsManager<E, K> manager,
			LabelWidgetFactory<E> factory,
			final SuggesionLabelFactory<E> factory2, int maxSize) {
		this.manager = manager;
		this.factory = factory;
		selectedLabels = new ArrayList<E>();
		panel = new PanelWithMore<LabelWidget<E>>(maxSize);
		initWidget(panel);

		addLabel = new Label(addLabelSign);
		panel.setExtraWidget(addLabel);
		addLabel.addClickHandler(new ClickHandler() {

			private List<E> allLabels;

			private LabelSuggestion<E> box;

			public void onClick(ClickEvent event) {
				allLabels = manager.getUserDefinedLabels();
				allLabels.removeAll(getSelectedLabels());

				box = new LabelSuggestion<E>(allLabels, factory2);
				panel.setExtraWidget(box);
				panel.setShowMoreButton(false);

				box
					.addSelectionHandler(new SelectionHandler<SuggestionResult<E>>() {

						public void onSelection(
								SelectionEvent<SuggestionResult<E>> event) {
							GWT.log("on selection");
							SuggestionResult<E> selectedItem =
								event.getSelectedItem();
							if (selectedItem.isHasValue()) {
								if (selectedItem.isExisting()) {
									labelAdded(selectedItem.getExistingValue());
								} else {
									newLabelAdded(selectedItem.getNewValue());
								}
							}
							end();
						}

					});
				box.focus();
			}

			private void end() {
				panel.setExtraWidget(addLabel);
				panel.setShowMoreButton(true);
			}

		});
	}

	protected void addLabel(E label) {
		getSelectedLabels().add(label);
		LabelWidget<E> labelWidget = factory.createWidget(label);
		panel.add(labelWidget);
		labelWidget.addRemoveHandler(this);
	}

	protected void removeLabel(LabelWidget<E> labelWidget) {
		getSelectedLabels().remove(labelWidget.getLabel());
		panel.remove(labelWidget);
	}

	protected void removeLabel(E label) {
		getSelectedLabels().remove(label);
		for (LabelWidget<E> w : panel.getAll()) {
			if (w.getLabel().equals(label)) {
				panel.remove(w);
				break;
			}
		}
	}

	protected abstract void newLabelAdded(String name);

	protected abstract void labelAdded(E label);

	protected abstract void labelRemoved(LabelWidget<E> labelWidget);

	public void onRemove(RemoveEvent e) {
		if (e.getSource() instanceof LabelWidget) {
			@SuppressWarnings("unchecked")
			LabelWidget<E> toBeRemoved = (LabelWidget<E>) e.getSource();
			labelRemoved(toBeRemoved);
		}
	}

	public List<E> getSelectedLabels() {
		return selectedLabels;
	}
}
