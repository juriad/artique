package cz.artique.client.shortcuts;

import com.google.gwt.event.shared.GwtEvent;

import cz.artique.shared.model.shortcut.Shortcut;

public class ShortcutEvent extends GwtEvent<ShortcutHandler> {

	private static final Type<ShortcutHandler> TYPE =
		new Type<ShortcutHandler>();
	private final Shortcut shortcut;

	public static Type<ShortcutHandler> getType() {
		return TYPE;
	}

	public ShortcutEvent(Shortcut shortcut) {
		this.shortcut = shortcut;
	}

	@Override
	public final Type<ShortcutHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ShortcutHandler handler) {
		handler.onShortcut(this);
	}

	public Shortcut getShortcut() {
		return shortcut;
	}

}
