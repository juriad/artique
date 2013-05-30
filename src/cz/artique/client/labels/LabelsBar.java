package cz.artique.client.labels;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;

import cz.artique.client.common.AddButton;
import cz.artique.client.common.PanelWithMore;
import cz.artique.client.labels.suggestion.LabelSuggestion;
import cz.artique.client.labels.suggestion.SuggestionResult;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.LabelType;

public abstract class LabelsBar extends Composite implements HasEnabled {

	class LabelCloseHandler implements CloseHandler<LabelWidget> {
		public void onClose(CloseEvent<LabelWidget> e) {
			if (e.getSource() instanceof LabelWidget) {
				LabelWidget toBeRemoved = (LabelWidget) e.getSource();
				labelRemoved(toBeRemoved);
			}
		}
	}

	private AddButton<LabelsBar> addLabel;

	private List<Label> selectedLabels;

	private final PanelWithMore<LabelWidget> panel;

	private LabelWidgetFactory factory;

	private final CloseHandler<LabelWidget> labelCloseHandler =
		new LabelCloseHandler();

	private boolean enabled = true;

	public LabelsBar(LabelWidgetFactory factory) {
		this.factory = factory;
		selectedLabels = new ArrayList<Label>();
		panel = new PanelWithMore<LabelWidget>();
		initWidget(panel);

		setStylePrimaryName("labels-bar");

		addLabel = AddButton.FACTORY.createWidget(this);
		panel.setExtraWidget(addLabel);
		addLabel.addOpenHandler(new OpenHandler<LabelsBar>() {

			private List<Label> allLabels;

			private LabelSuggestion box;

			public void onOpen(OpenEvent<LabelsBar> event) {
				allLabels =
					Managers.LABELS_MANAGER.getLabels(LabelType.USER_DEFINED);
				allLabels.removeAll(getSelectedLabels());

				box = new LabelSuggestion(allLabels, true);
				box.setStylePrimaryName("labels-bar-suggest-box");
				panel.setExtraWidget(box);

				box
					.addSelectionHandler(new SelectionHandler<SuggestionResult>() {

						public void onSelection(
								SelectionEvent<SuggestionResult> event) {
							GWT.log("on selection");
							SuggestionResult selectedItem =
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
			}

		});
	}

	protected void addLabel(Label label) {
		getSelectedLabels().add(label);
		LabelWidget labelWidget = factory.createWidget(label);
		panel.add(labelWidget);
		labelWidget.addCloseHandler(labelCloseHandler);
	}

	protected void removeLabel(LabelWidget labelWidget) {
		getSelectedLabels().remove(labelWidget.getLabel());
		panel.remove(labelWidget);
	}

	protected void removeLabel(Label label) {
		getSelectedLabels().remove(label);
		for (LabelWidget w : panel.getWidgets()) {
			if (w.getLabel().equals(label)) {
				panel.remove(w);
				break;
			}
		}
	}

	protected abstract void newLabelAdded(String name);

	protected abstract void labelAdded(Label label);

	protected abstract void labelRemoved(LabelWidget labelWidget);

	public List<Label> getSelectedLabels() {
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

		for (LabelWidget labelWidget : panel.getWidgets()) {
			labelWidget.setEnabled(enabled);
		}

		addLabel.setVisible(enabled);
	}
}
