package cz.artique.client.labels.suggestion;

import java.util.List;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
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

import cz.artique.client.manager.Managers;
import cz.artique.shared.model.label.Label;

public class LabelSuggestion extends Composite
		implements HasSelectionHandlers<SuggestionResult> {

	private final TextBox textBox;
	private final SuggestionPopup popup;

	private boolean complete = false;
	private boolean allowNewValue;
	private List<Label> allLabels;

	public LabelSuggestion(List<Label> allLabels, boolean allowNewValue) {
		this.allLabels = allLabels;
		this.allowNewValue = allowNewValue;

		FlowPanel panel = new FlowPanel();
		initWidget(panel);

		textBox = new TextBox();
		panel.add(textBox);

		// TODO settings maxItems in poput: 20
		popup = new SuggestionPopup(20);
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
				event.stopPropagation();
			}
		});

		textBox.addKeyUpHandler(new KeyUpHandler() {

			private String oldText = null;

			public void onKeyUp(KeyUpEvent event) {
				if (oldText == null || !oldText.equals(textBox.getText())) {
					oldText = textBox.getText();
					textChanged();
				}
				event.stopPropagation();
			}
		});

		textBox.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				event.stopPropagation();
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

		popup.addSelectionHandler(new SelectionHandler<Label>() {

			public void onSelection(SelectionEvent<Label> e) {
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
				SelectionEvent.fire(this, new SuggestionResult());
			} else {
				SelectionEvent.fire(this, new SuggestionResult(textBox
					.getValue()
					.trim()));
			}
		} else {
			Label firstAvaliable = popup.getFirstAvaliable();
			if (firstAvaliable != null) {
				SelectionEvent.fire(this, new SuggestionResult(firstAvaliable));
			} else {
				SelectionEvent.fire(this, new SuggestionResult());
			}
		}
	}

	protected void saveExistingValue() {
		complete();
		SelectionEvent.fire(this,
			new SuggestionResult(popup.getSelectedValue()));
	}

	protected void textChanged() {
		String text = textBox.getText();
		if (text.length() > 0) {
			List<Label> prefixes =
				Managers.LABELS_MANAGER.fullTextSearch(text, allLabels);
			popup.setData(prefixes);

			if (prefixes.isEmpty()) {
				popup.setVisible(false);
			} else {
				popup.setVisible(true);
			}
		} else {
			popup.setVisible(false);
		}
	}

	protected void tab(KeyDownEvent event) {
		if (popup.isVisible()) {
			if (popup.getSelectedValue() == null) {
				popup.setFocused(0);
			}
			textBox.setText(popup.getSelectedValue().getDisplayName());
			textChanged();
			event.preventDefault();
			event.stopPropagation();
		}
	}

	protected void cancel() {
		complete();
		SelectionEvent.fire(this, new SuggestionResult());
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
			SelectionHandler<SuggestionResult> handler) {
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
