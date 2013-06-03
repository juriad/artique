package cz.artique.client.common;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.ListBox;

public class EnumListPicker<E extends Enum<E>> extends Composite
		implements HasEnabled, HasValue<E> {

	private final List<E> enums = new ArrayList<E>();

	public EnumListPicker(E enu, String group, ConstantsWithLookup constants) {
		listBox = new ListBox();
		initWidget(listBox);

		Object[] enumConstants = enu.getClass().getEnumConstants();
		for (int i = 0; i < enumConstants.length; i++) {
			@SuppressWarnings("unchecked")
			E e = (E) enumConstants[i];
			String name = group + "_" + e.name();
			String string = constants.getString(name);
			listBox.addItem(string, e.name());
			enums.add(e);
			if (enu.equals(e)) {
				listBox.setSelectedIndex(i);
			}
		}
	}

	private boolean enabled;

	private ListBox listBox;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		listBox.setEnabled(enabled);
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<E> handler) {
		return listBox.addHandler(handler, ValueChangeEvent.getType());
	}

	public E getValue() {
		int selectedIndex = listBox.getSelectedIndex();
		return selectedIndex >= 0 ? enums.get(selectedIndex) : null;
	}

	public void setValue(E value) {
		setValue(value, true);
	}

	public void setValue(E value, boolean fireEvents) {
		if (value == null) {
			return;
		}
		int index = enums.indexOf(value);
		listBox.setSelectedIndex(index);
	}
}
