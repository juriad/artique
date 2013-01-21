package cz.artique.client.labels;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestBox.DefaultSuggestionDisplay;
import com.google.gwt.user.client.ui.SuggestBox.SuggestionDisplay;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

import cz.artique.shared.utils.HasKey;
import cz.artique.shared.utils.HasName;

public abstract class LabelsBar<E extends HasName & HasKey<K>, K>
		extends Composite implements RemoveHandler {
	private static final String addLabelSign = "+";

	private Label addLabel;

	private List<E> selectedLabels;

	private final PanelWithMore<LabelWidget<E>> panel;

	protected final LabelsManager<E, K> manager;

	private LabelWidgetFactory<E> factory;

	public LabelsBar(final LabelsManager<E, K> manager,
			LabelWidgetFactory<E> factory, int maxSize) {
		this.manager = manager;
		this.factory = factory;
		selectedLabels = new ArrayList<E>();
		panel = new PanelWithMore<LabelWidget<E>>(maxSize);
		initWidget(panel);

		addLabel = new Label(addLabelSign);
		panel.setExtraWidget(addLabel);
		addLabel.addClickHandler(new ClickHandler() {

			private List<E> allLabels;

			private SuggestBox box;

			public void onClick(ClickEvent event) {
				complete = false;
				MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
				allLabels = manager.getUserDefinedLabels();
				allLabels.removeAll(getSelectedLabels());
				for (E e : allLabels) {
					oracle.add(e.getName());
				}

				box = new SuggestBox(oracle);
				box.setAutoSelectEnabled(false);
				panel.setExtraWidget(box);
				panel.setShowMoreButton(false);

				box.getValueBox().addBlurHandler(new BlurHandler() {

					public void onBlur(BlurEvent event) {
						DefaultSuggestionDisplay suggestionDisplay =
							(DefaultSuggestionDisplay) box
								.getSuggestionDisplay();
						boolean suggestionListShowing =
							suggestionDisplay.isSuggestionListShowing();
						GWT.log("showing: " + suggestionListShowing);

						String value = box.getValue().trim();
						if (!value.isEmpty()) {
							save(value);
						} else {
							cancel();
						}
					}
				});
				box.addKeyDownHandler(new KeyDownHandler() {

					public void onKeyDown(KeyDownEvent event) {
						switch (event.getNativeKeyCode()) {
						case KeyCodes.KEY_ENTER:
						case KeyCodes.KEY_TAB:
							String value = box.getValue().trim();
							if (!value.isEmpty()) {
								save(value);
							} else {
								cancel();
							}
							break;
						case KeyCodes.KEY_ESCAPE:
							cancel();
							break;
						}
					}
				});

				box.addSelectionHandler(new SelectionHandler<Suggestion>() {
					public void onSelection(SelectionEvent<Suggestion> event) {
						GWT.log("selection: " + event.getSelectedItem());
						save(event.getSelectedItem().getReplacementString());
					}
				});
				box.setFocus(true);
			}

			private boolean complete = false;

			public void save(String value) {
				if (!begin())
					return;
				GWT.log("save " + value);
				boolean added = false;
				for (E e : allLabels) {
					if (e.getName().equals(value)) {
						if (!getSelectedLabels().contains(e)) {
							labelAdded(e);
						}
						added = true;
						break;
					}
				}
				if (!added) {
					newLabelAdded(value);
				}
				end();
			}

			public void cancel() {
				if (!begin())
					return;
				GWT.log("cancel");
				end();
			}

			private boolean begin() {
				if (complete)
					return false;
				complete = true;
				return true;
			}

			private void end() {
				box.getValueBox().setFocus(false);
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
