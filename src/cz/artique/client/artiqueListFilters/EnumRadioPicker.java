package cz.artique.client.artiqueListFilters;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.RadioButton;

import cz.artique.client.i18n.ArtiqueConstants;
import cz.artique.client.i18n.ArtiqueI18n;
import cz.artique.client.utils.InlineFlowPanel;

public class EnumRadioPicker<E extends Enum<E>> extends Composite
		implements HasEnabled, HasValue<E> {
	public class ValueHandler implements ValueChangeHandler<Boolean> {

		public void onValueChange(ValueChangeEvent<Boolean> event) {
			if (event.getValue()) {
				RadioButton rb = (RadioButton) event.getSource();
				E newValue = buttons.get(rb);
				ValueChangeEvent.fireIfNotEqual(EnumRadioPicker.this, selected,
					newValue);
				selected = newValue;
			}
		}
	}

	private E selected = null;

	private final InlineFlowPanel panel;

	private final Map<RadioButton, E> buttons = new HashMap<RadioButton, E>();
	private final Map<E, RadioButton> enums = new HashMap<E, RadioButton>();

	public EnumRadioPicker(E enu, String group) {
		panel = new InlineFlowPanel();
		initWidget(panel);

		ArtiqueConstants constants = ArtiqueI18n.I18N.getConstants();
		Object[] enumConstants = enu.getClass().getEnumConstants();
		for (int i = 0; i < enumConstants.length; i++) {
			@SuppressWarnings("unchecked")
			E e = (E) enumConstants[i];
			String name = group + "_" + e.name();
			RadioButton button =
				new RadioButton(group, constants.getString(name));
			button.addValueChangeHandler(new ValueHandler());
			if (enu.equals(e)) {
				button.setValue(true);
				selected = e;
			}
			panel.add(button);

			buttons.put(button, e);
			enums.put(e, button);
		}
	}

	private boolean enabled;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		for (RadioButton rb : buttons.keySet()) {
			rb.setEnabled(enabled);
		}
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<E> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	public E getValue() {
		return selected;
	}

	public void setValue(E value) {
		setValue(value, true);
	}

	public void setValue(E value, boolean fireEvents) {
		if (value == null) {
			return;
		}
		RadioButton radioButton = enums.get(value);
		radioButton.setValue(true, fireEvents);
	}
}
