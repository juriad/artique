package cz.artique.client.labels.suggestion;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ValueLabel;
import com.google.gwt.user.client.ui.VerticalPanel;

import cz.artique.client.labels.LabelRenderer;
import cz.artique.shared.model.label.Label;

public class SuggestionPopup extends Composite
		implements HasSelectionHandlers<Label> {

	private boolean mouseOver = false;

	class MouseHandlers implements MouseOverHandler, MouseOutHandler {

		public void onMouseOut(MouseOutEvent event) {
			mouseOver = false;
		}

		public void onMouseOver(MouseOverEvent event) {
			@SuppressWarnings("unchecked")
			ValueLabel<Label> source = (ValueLabel<Label>) event.getSource();

			int oldFocused = focused;
			int index = valueLabels.indexOf(source);
			if (index >= 0) {
				focused = index;
			}
			changeFocus(oldFocused, focused);

			mouseOver = true;
		}
	}

	private final List<ValueLabel<Label>> valueLabels =
		new ArrayList<ValueLabel<Label>>();
	private final com.google.gwt.user.client.ui.Label moreLabels;

	private final VerticalPanel panel;
	private final int maxItems;
	private Label selectedValue;

	private int focused = -1;
	private int actualSize = 0;

	public SuggestionPopup(int maxItems) {
		this.maxItems = maxItems;
		panel = new VerticalPanel();
		initWidget(panel);
		for (int i = 0; i < maxItems; i++) {
			ValueLabel<Label> l = createLabel();
			l.setVisible(false);
			l.addDomHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					@SuppressWarnings("unchecked")
					ValueLabel<Label> source =
						(ValueLabel<Label>) event.getSource();
					Label value = source.getValue();
					setSelectedValue(value);
					SelectionEvent.fire(SuggestionPopup.this, value);
				}
			}, ClickEvent.getType());
			l.addDomHandler(new MouseHandlers(), MouseOverEvent.getType());
			l.addDomHandler(new MouseHandlers(), MouseOutEvent.getType());

			valueLabels.add(l);
			panel.add(l);
		}

		setStylePrimaryName("labelSuggestionPopup");

		moreLabels = new com.google.gwt.user.client.ui.Label();
		moreLabels.setVisible(false);
		moreLabels.addStyleName("labelSuggestionPopupMore");
		panel.add(moreLabels);
	}

	protected void setSelectedValue(Label value) {
		this.selectedValue = value;
	}

	public Label getSelectedValue() {
		return selectedValue;
	}

	public void setData(List<Label> labels) {
		actualSize = Math.min(labels.size(), maxItems);
		for (int i = 0; i < actualSize; i++) {
			ValueLabel<Label> valueLabel = valueLabels.get(i);
			valueLabel.setValue(labels.get(i));
			valueLabel.setVisible(true);
			valueLabel.setStyleDependentName("selected", false);
		}
		for (int i = actualSize; i < maxItems; i++) {
			ValueLabel<Label> valueLabel = valueLabels.get(i);
			valueLabel.setVisible(false);
			valueLabel.setStyleDependentName("selected", false);
		}

		if (labels.size() > maxItems) {
			moreLabels.setVisible(true);
			moreLabels.setText("+ " + (labels.size() - maxItems));
		} else {
			moreLabels.setVisible(false);
		}

		focused = -1;
		selectedValue = null;
	}

	public Label getFirstAvaliable() {
		if (valueLabels.size() > 0) {
			ValueLabel<Label> valueLabel = valueLabels.get(0);
			if (valueLabel.isVisible()) {
				return valueLabel.getValue();
			}
		}
		return null;
	}

	public int getMaxItems() {
		return maxItems;
	}

	public boolean up() {
		int oldFocused = focused;

		if (focused == -1) {
			focused = actualSize - 1;
		} else if (focused == 0) {
			focused = -1;
		} else {
			focused--;
		}

		changeFocus(oldFocused, focused);
		return focused >= 0;
	}

	public boolean down() {
		int oldFocused = focused;

		if (focused == -1) {
			focused = 0;
		} else if (focused == actualSize - 1) {
			focused = -1;
		} else {
			focused++;
		}

		changeFocus(oldFocused, focused);
		return focused >= 0;
	}

	public boolean isMouseOver() {
		return mouseOver;
	}

	private void changeFocus(int oldFocused, int newFocused) {
		if (oldFocused == newFocused) {
			return;
		}

		if (oldFocused >= 0) {
			valueLabels
				.get(oldFocused)
				.setStyleDependentName("selected", false);
		}

		if (newFocused >= 0) {
			ValueLabel<Label> valueLabel = valueLabels.get(newFocused);
			valueLabel.setStyleDependentName("selected", true);
			selectedValue = valueLabel.getValue();
		} else {
			selectedValue = null;
		}
	}

	public void setFocused(int index) {
		if (index < -1 || index >= actualSize) {
			return;
		}
		int oldFocused = focused;
		focused = index;
		changeFocus(oldFocused, focused);
	}

	public HandlerRegistration addSelectionHandler(
			SelectionHandler<Label> handler) {
		return addHandler(handler, SelectionEvent.getType());
	}

	private final LabelRenderer labelRenderer = new LabelRenderer(true);

	public ValueLabel<Label> createLabel() {
		ValueLabel<Label> valueLabel = new ValueLabel<Label>(labelRenderer);
		valueLabel.setStylePrimaryName("labelSuggestionLabel");
		return valueLabel;
	}
}
