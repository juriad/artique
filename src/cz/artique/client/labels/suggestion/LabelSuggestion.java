package cz.artique.client.labels.suggestion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
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
import cz.artique.shared.utils.HasKey;
import cz.artique.shared.utils.HasName;

public class LabelSuggestion<E extends HasName & Comparable<E> & HasKey<?>>
		extends Composite implements HasSelectionHandlers<SelectionResult<E>> {

	private final TextBox textBox;
	private final SuggestionPopup<E> popup;
	private List<E> labels;

	public LabelSuggestion(LabelsManager<E, ?> manager, List<E> labels,
			SuggesionLabelFactory<E> factory) {
		this.labels = labels;
		Collections.sort(this.labels);

		FlowPanel panel = new FlowPanel();
		initWidget(panel);

		textBox = new TextBox();
		panel.add(textBox);

		// TODO settings maxItems in poput: 20
		popup = new SuggestionPopup<E>(factory, 20);
		panel.add(popup);

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
					break;
				case KeyCodes.KEY_TAB:
					tab();
					break;
				default:
					textChanged();
					break;
				}
			}
		});

		textBox.addMouseOverHandler(new MouseOverHandler() {

			public void onMouseOver(MouseOverEvent event) {
				if (popup.isVisible()) {
					popup.setFocused(-1);
				}
			}
		});

		textBox.addBlurHandler(new BlurHandler() {

			public void onBlur(BlurEvent event) {
				if (popup.isVisible() && popup.isMouseOver()) {
					// clicked on asuggestion
					// wait for GeneralClickEvent
				} else {
					saveNewValue();
				}
			}
		});

		popup.addGeneralClickHandler(new GeneralClickHandler() {

			public void onClick(GeneralClickEvent e) {
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
		if (textBox.getValue().trim().isEmpty()) {
			SelectionEvent.fire(this, new SelectionResult<E>());
		} else {
			SelectionEvent.fire(this, new SelectionResult<E>(textBox
				.getValue()
				.trim()));
		}
	}

	protected void saveExistingValue() {
		SelectionEvent.fire(this,
			new SelectionResult<E>(popup.getSelectedValue()));
	}

	protected void textChanged() {
		String text = textBox.getText();
		if (text.length() > 0) {
			List<E> prefixes = findByPrefix(text);
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

	protected void tab() {
		if (popup.isVisible()) {
			if (popup.getSelectedValue() != null) {
				textBox.setText(popup.getSelectedValue().getName());
				textChanged();
			}
		}
	}

	protected void cancel() {
		popup.setVisible(false);
		SelectionEvent.fire(this, new SelectionResult<E>());
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

	private List<E> findByPrefix(String prefix) {
		List<E> l = new ArrayList<E>();
		for (E e : labels) {
			if (e.getName().toLowerCase().startsWith(prefix)) {
				l.add(e);
			}
		}
		return l;
	}

	public HandlerRegistration addSelectionHandler(
			SelectionHandler<SelectionResult<E>> handler) {
		return addHandler(handler, SelectionEvent.getType());
	}
}
