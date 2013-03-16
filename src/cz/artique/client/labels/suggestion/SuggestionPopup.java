package cz.artique.client.labels.suggestion;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ValueLabel;
import com.google.gwt.user.client.ui.VerticalPanel;

import cz.artique.client.artiqueLabels.ActionEvent;
import cz.artique.client.artiqueLabels.ActionHandler;
import cz.artique.client.artiqueLabels.HasActionHandlers;
import cz.artique.shared.utils.HasDisplayName;

public class SuggestionPopup<E extends HasDisplayName> extends Composite
		implements HasActionHandlers {

	private boolean mouseOver = false;

	class MouseHandlers implements MouseOverHandler, MouseOutHandler {

		public void onMouseOut(MouseOutEvent event) {
			mouseOver = false;
		}

		public void onMouseOver(MouseOverEvent event) {
			@SuppressWarnings("unchecked")
			ValueLabel<E> source = (ValueLabel<E>) event.getSource();

			int oldFocused = focused;
			int index = valueLabels.indexOf(source);
			if (index >= 0) {
				focused = index;
			}
			changeFocus(oldFocused, focused);

			mouseOver = true;
		}
	}

	private final List<ValueLabel<E>> valueLabels =
		new ArrayList<ValueLabel<E>>();
	private final Label moreLabels;

	private final VerticalPanel panel;
	private final int maxItems;
	private E selectedValue;

	private int focused = -1;
	private int actualSize = 0;

	public SuggestionPopup(SuggesionLabelFactory<E> factory, int maxItems) {
		this.maxItems = maxItems;
		panel = new VerticalPanel();
		initWidget(panel);
		for (int i = 0; i < maxItems; i++) {
			ValueLabel<E> l = factory.createLabel();
			l.setVisible(false);
			l.addDomHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					@SuppressWarnings("unchecked")
					ValueLabel<E> source = (ValueLabel<E>) event.getSource();
					E value = source.getValue();
					setSelectedValue(value);
					fireEvent(new ActionEvent());
				}

			}, ClickEvent.getType());
			l.addDomHandler(new MouseHandlers(), MouseOverEvent.getType());
			l.addDomHandler(new MouseHandlers(), MouseOutEvent.getType());

			valueLabels.add(l);
			panel.add(l);
		}

		moreLabels = new Label();
		moreLabels.setVisible(false);
		moreLabels.addStyleName("more");
		panel.add(moreLabels);
	}

	protected void setSelectedValue(E value) {
		this.selectedValue = value;
	}

	public E getSelectedValue() {
		return selectedValue;
	}

	public HandlerRegistration addActionHandler(
			ActionHandler handler) {
		return addHandler(handler, ActionEvent.getType());
	}

	public void setData(List<E> labels) {
		actualSize = Math.min(labels.size(), maxItems);
		for (int i = 0; i < actualSize; i++) {
			ValueLabel<E> valueLabel = valueLabels.get(i);
			valueLabel.setValue(labels.get(i));
			valueLabel.setVisible(true);
		}
		for (int i = actualSize; i < maxItems; i++) {
			ValueLabel<E> valueLabel = valueLabels.get(i);
			valueLabel.setVisible(false);
		}

		if (labels.size() > maxItems) {
			moreLabels.setText("+ " + (labels.size() - maxItems));
		}

		focused = -1;
		selectedValue = null;
	}

	public E getFirstAvaliable() {
		if (valueLabels.size() > 0) {
			ValueLabel<E> valueLabel = valueLabels.get(0);
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
			valueLabels.get(oldFocused).removeStyleName("selected");
		}

		if (newFocused >= 0) {
			ValueLabel<E> valueLabel = valueLabels.get(newFocused);
			valueLabel.addStyleName("selected");
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
}
