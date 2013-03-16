package cz.artique.client.labels;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.InlineLabel;

import cz.artique.client.labels.suggestion.LabelSuggestion;
import cz.artique.client.labels.suggestion.SuggesionLabelFactory;
import cz.artique.client.labels.suggestion.SuggestionResult;
import cz.artique.shared.model.label.LabelType;
import cz.artique.shared.utils.HasDisplayName;
import cz.artique.shared.utils.HasKey;

public abstract class LabelsBar<E extends HasDisplayName & HasKey<K> & Comparable<E>, K>
		extends Composite implements HasEnabled {
	private static final String addLabelSign = "+";

	class LabelRemoveHandler implements RemoveHandler {
		public void onRemove(RemoveEvent e) {
			if (e.getSource() instanceof LabelWidget) {
				@SuppressWarnings("unchecked")
				LabelWidget<E> toBeRemoved = (LabelWidget<E>) e.getSource();
				labelRemoved(toBeRemoved);
			}
		}
	}

	private InlineLabel addLabel;

	private List<E> selectedLabels;

	private final PanelWithMore<LabelWidget<E>> panel;

	protected final LabelsManager<E, K> manager;

	private LabelWidgetFactory<E> factory;

	private final RemoveHandler labelRemoveHandler = new LabelRemoveHandler();

	private boolean enabled = true;

	public LabelsBar(final LabelsManager<E, K> manager,
			LabelWidgetFactory<E> factory,
			final SuggesionLabelFactory<E> factory2, int maxSize) {
		this.manager = manager;
		this.factory = factory;
		selectedLabels = new ArrayList<E>();
		panel = new PanelWithMore<LabelWidget<E>>(maxSize);
		initWidget(panel);

		setStylePrimaryName("labels-bar");

		addLabel = new InlineLabel(addLabelSign);
		addLabel.setStylePrimaryName("labels-bar-add-button");
		panel.setExtraWidget(addLabel);
		addLabel.addClickHandler(new ClickHandler() {

			private List<E> allLabels;

			private LabelSuggestion<E> box;

			public void onClick(ClickEvent event) {
				allLabels = manager.getLabels(LabelType.USER_DEFINED);
				allLabels.removeAll(getSelectedLabels());

				box = new LabelSuggestion<E>(manager, allLabels, factory2, true);
				box.setStylePrimaryName("labels-bar-suggest-box");
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
		labelWidget.addRemoveHandler(labelRemoveHandler);
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

	public List<E> getSelectedLabels() {
		return selectedLabels;
	}

	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Incorrect if new label is being added
	 * 
	 * @see com.google.gwt.user.client.ui.HasEnabled#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
		if (enabled == this.enabled) {
			return;
		}
		this.enabled = enabled;

		for (LabelWidget<E> labelWidget : panel.getAll()) {
			labelWidget.setEnabled(enabled);
		}

		addLabel.setVisible(enabled);
	}
}
