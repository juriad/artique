package cz.artique.client.shortcuts;

import com.google.gwt.event.shared.GwtEvent;

import cz.artique.shared.model.shortcut.Shortcut;

/**
 * Describes a shortcut which has been triggered by pressing key combination and
 * shall be processed.
 * 
 * @author Adam Juraszek
 * 
 */
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

	/**
	 * @return the {@link Shortcut} to be processed
	 */
	public Shortcut getShortcut() {
		return shortcut;
	}

}
