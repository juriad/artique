package cz.artique.client.shortcuts;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import cz.artique.shared.model.shortcut.Shortcut;
import cz.artique.shared.model.shortcut.ShortcutAction;
import cz.artique.shared.model.shortcut.ShortcutType;

public class ActionShortcutEditor extends Composite
		implements HasValue<Shortcut> {
	private static ActionShortcutEditorUiBinder uiBinder = GWT
		.create(ActionShortcutEditorUiBinder.class);

	interface ActionShortcutEditorUiBinder
			extends UiBinder<Widget, ActionShortcutEditor> {}

	@UiField
	TextBox keyStroke;

	@UiField
	ActionPicker action;

	public ActionShortcutEditor() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Shortcut> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	public Shortcut getValue() {
		Shortcut s = new Shortcut();
		s.setAction(action.getValue());
		s.setKeyStroke(keyStroke.getValue());
		s.setType(ShortcutType.ACTION);
		return s;
	}

	public void setValue(Shortcut value) {
		setValue(value, true);
	}

	public void setValue(Shortcut value, boolean fireEvents) {
		action.setValue(ShortcutAction.REFRESH);
		keyStroke.setValue(null);
	}

}
