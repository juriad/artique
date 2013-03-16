package cz.artique.client.labels.suggestion;

import java.util.List;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;

import cz.artique.client.artiqueLabels.GeneralClickEvent;
import cz.artique.client.artiqueLabels.GeneralClickHandler;
import cz.artique.client.labels.LabelsManager;
import cz.artique.shared.utils.HasDisplayName;
import cz.artique.shared.utils.HasKey;

public class LabelSuggestion<E extends HasDisplayName & Comparable<E> & HasKey<?>>
		extends Composite implements HasSelectionHandlers<SuggestionResult<E>> {

	private final TextBox textBox;
	private final SuggestionPopup<E> popup;

	private boolean complete = false;
	private boolean allowNewValue;
	private LabelsManager<E, ?> manager;
	private List<E> allLabels;

	public LabelSuggestion(LabelsManager<E, ?> manager, List<E> allLabels,
			SuggesionLabelFactory<E> factory, boolean allowNewValue) {
		this.manager = manager;
		this.allLabels = allLabels;
		this.allowNewValue = allowNewValue;

		FlowPanel panel = new FlowPanel();
		initWidget(panel);

		textBox = new TextBox();
		panel.add(textBox);

		// TODO settings maxItems in poput: 20
		popup = new SuggestionPopup<E>(factory, 20);
		panel.add(popup);

		textBox.addBlurHandler(new BlurHandler() {

			public void onBlur(BlurEvent event) {
				if (complete) {
					return;
				}
				if (popup.isVisible() && popup.isMouseOver()) {
					// clicked on a suggestion
					// wait for GeneralClickEvent
				} else {
					saveNewValue();
				}
			}
		});

		textBox.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				switch (event.getNativeKeyCode()) {
				case KeyCodes.KEY_UP:
					keyUp();
					break;
				case KeyCodes.KEY_DOWN:
					keyDown();
					break;
				case KeyCodes.KEY_ENTER:
					enter();
					break;
				case KeyCodes.KEY_ESCAPE:
					cancel();
					event.preventDefault();
					event.stopPropagation();
					break;
				case KeyCodes.KEY_TAB:
					tab(event);
					break;
				}
			}
		});

		textBox.addKeyUpHandler(new KeyUpHandler() {

			private String oldText = null;

			public void onKeyUp(KeyUpEvent event) {
				if (oldText == null || !oldText.equals(textBox.getText())) {
					oldText = textBox.getText();
					textChanged();
				}
			}
		});

		textBox.addMouseOverHandler(new MouseOverHandler() {

			public void onMouseOver(MouseOverEvent event) {
				if (complete) {
					return;
				}
				if (popup.isVisible()) {
					popup.setFocused(-1);
				}
			}
		});

		popup.addGeneralClickHandler(new GeneralClickHandler() {

			public void onClick(GeneralClickEvent e) {
				if (complete) {
					return;
				}
				if (popup.isVisible() && popup.getSelectedValue() != null) {
					saveExistingValue();
				}
			}
		});
	}

	protected void enter() {
		if (popup.isVisible() && popup.getSelectedValue() != null) {
			saveExistingValue();
		} else {
			saveNewValue();
		}
	}

	protected void saveNewValue() {
		complete();

		if (allowNewValue) {
			if (textBox.getValue().trim().isEmpty()) {
				SelectionEvent.fire(this, new SuggestionResult<E>());
			} else {
				SelectionEvent.fire(this, new SuggestionResult<E>(textBox
					.getValue()
					.trim()));
			}
		} else {
			E firstAvaliable = popup.getFirstAvaliable();
			if (popup.isVisible() && firstAvaliable != null) {
				SelectionEvent.fire(this, new SuggestionResult<E>(
					firstAvaliable));
			} else {
				SelectionEvent.fire(this, new SuggestionResult<E>());
			}
		}
	}

	protected void saveExistingValue() {
		complete();
		SelectionEvent.fire(this,
			new SuggestionResult<E>(popup.getSelectedValue()));
	}

	protected void textChanged() {
		String text = textBox.getText();
		if (text.length() > 0) {
			List<E> prefixes = manager.fullTextSearch(text, allLabels);

			if (prefixes.isEmpty()) {
				popup.setVisible(false);
			} else {
				popup.setData(prefixes);
				popup.setVisible(true);
			}
		} else {
			popup.setVisible(false);
		}
	}

	protected void tab(KeyDownEvent event) {
		if (popup.isVisible()) {
			if (popup.getSelectedValue() != null) {
				textBox.setText(popup.getSelectedValue().getDisplayName());
				textChanged();
				event.preventDefault();
				event.stopPropagation();
			}
		}
	}

	protected void cancel() {
		complete();
		SelectionEvent.fire(this, new SuggestionResult<E>());
	}

	protected void keyDown() {
		if (popup.isVisible()) {
			popup.down();
		}
	}

	protected void keyUp() {
		if (popup.isVisible()) {
			popup.up();
		}
	}

	public HandlerRegistration addSelectionHandler(
			SelectionHandler<SuggestionResult<E>> handler) {
		return addHandler(handler, SelectionEvent.getType());
	}

	public void focus() {
		textBox.setFocus(true);
	}

	private void complete() {
		if (complete) {
			return;
		}
		complete = true;
		popup.setVisible(false);
		textBox.setFocus(false);
	}
}
