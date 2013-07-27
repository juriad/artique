package cz.artique.client.labels.suggestion;

import java.util.List;

import com.google.gwt.core.client.GWT;
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
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;

import cz.artique.shared.model.label.Label;

/**
 * Reimplementation of {@link SuggestBox} for {@link Label}s; it allows user to
 * select a non-existent {@link Label}.
 * 
 * @author Adam Juraszek
 * 
 */
public class LabelSuggestion extends Composite
		implements HasSelectionHandlers<SuggestionResult> {

	interface MyResources extends ClientBundle {
		@NotStrict
		@Source("LabelSuggestion.css")
		CssResource style();
	}

	private static final MyResources res = GWT.create(MyResources.class);

	private final TextBox textBox;
	private final SuggestionPopup popup;

	private boolean complete = false;
	private LabelsPool pool;

	/**
	 * Creates a new suggestion box with a {@link LabelsPool} of available
	 * {@link Label}s and maximum number of {@link Label}s to be shown.
	 * 
	 * The suggestion reacts to UP, DOWN, ENTER, ESCAPE and TAB keys.
	 * 
	 * @param pool
	 *            {@link LabelsPool}
	 * @param maxItems
	 *            maximum number of {@link Label}s to be shown
	 */
	public LabelSuggestion(LabelsPool pool, int maxItems) {
		res.style().ensureInjected();
		this.pool = pool;

		FlowPanel panel = new FlowPanel();
		initWidget(panel);

		textBox = new TextBox();
		panel.add(textBox);
		textBox.setStylePrimaryName("labelSuggestionBox");

		popup = new SuggestionPopup(maxItems);
		panel.add(popup);
		setStylePrimaryName("labelSuggestion");

		textBox.addBlurHandler(new BlurHandler() {
			public void onBlur(BlurEvent event) {
				if (complete) {
					return;
				}
				if (popup.isVisible() && popup.isMouseOver()) {
					// clicked on a suggestion
					// wait for SelectionEvent
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
					break;
				case KeyCodes.KEY_TAB:
					tab(event);
					break;
				}
				event.stopPropagation(); // disable global shortcuts
			}
		});

		textBox.addKeyUpHandler(new KeyUpHandler() {
			private String oldText = null;

			public void onKeyUp(KeyUpEvent event) {
				if (oldText == null || !oldText.equals(textBox.getText())) {
					oldText = textBox.getText();
					textChanged();
				}
				event.stopPropagation(); // disable global shortcuts
			}
		});

		textBox.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				event.stopPropagation(); // disable global shortcuts
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

	/**
	 * When enter was pressed.
	 */
	protected void enter() {
		if (popup.isVisible() && popup.getSelectedValue() != null) {
			saveExistingValue();
		} else {
			saveNewValue();
		}
	}

	/**
	 * When a name of non-existent {@link Label} was inserted.
	 */
	protected void saveNewValue() {
		complete();

		if (pool.isNewValueAllowed()) {
			if (textBox.getValue().trim().isEmpty()) { // no chance
				SelectionEvent.fire(this, new SuggestionResult());
			} else { // might work
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

	/**
	 * When a name of existing {@link Label} was inserted.
	 */
	protected void saveExistingValue() {
		complete();
		SelectionEvent.fire(this,
			new SuggestionResult(popup.getSelectedValue()));
	}

	/**
	 * When user presses any key causing the textbox change its content.
	 */
	protected void textChanged() {
		String text = textBox.getText();
		if (text.length() > 0) {
			List<Label> results = pool.fullTextSearch(text);
			popup.setData(results);
			popup.setVisible(results.size() > 0);
		} else {
			popup.setVisible(false);
		}
	}

	/**
	 * When tab was pressed.
	 * 
	 * @param event
	 *            event to be prevented to perform default action
	 */
	protected void tab(KeyDownEvent event) {
		if (popup.isVisible()) {
			Label selectedValue = popup.getSelectedValue();
			if (selectedValue == null) {
				selectedValue = popup.getFirstAvaliable();
			}
			if (selectedValue == null) {
				return;
			}
			textBox.setText(selectedValue.getDisplayName());
			textChanged();
			event.preventDefault();
		}
	}

	/**
	 * Cancels the suggestion.
	 */
	protected void cancel() {
		complete();
		SelectionEvent.fire(this, new SuggestionResult());
	}

	/**
	 * When key down was pressed.
	 */
	protected void keyDown() {
		if (popup.isVisible()) {
			popup.down();
		}
	}

	/**
	 * When key down was pressed.
	 */
	protected void keyUp() {
		if (popup.isVisible()) {
			popup.up();
		}
	}

	public HandlerRegistration addSelectionHandler(
			SelectionHandler<SuggestionResult> handler) {
		return addHandler(handler, SelectionEvent.getType());
	}

	/**
	 * Ends the suggestion event processing.
	 */
	private void complete() {
		if (complete) {
			return;
		}
		complete = true;
		popup.setVisible(false);
		textBox.setFocus(false);
	}

	/**
	 * Focuses (restores) the suggestion.
	 */
	public void focus() {
		textBox.setValue(null);
		textChanged();
		complete = false;
		textBox.setFocus(true);
	}
}
