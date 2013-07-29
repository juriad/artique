package cz.artique.client.shortcuts;

import com.google.gwt.event.shared.EventHandler;

import cz.artique.shared.model.shortcut.Shortcut;

/**
 * Handler called when there exists a {@link Shortcut} which shall be processed.
 * 
 * @author Adam Juraszek
 * 
 */
public interface ShortcutHandler extends EventHandler {
	/**
	 * Called whenever {@link ShortcutEvent} occurs.
	 * 
	 * @param e
	 *            {@link ShortcutEvent}
	 */
	void onShortcut(ShortcutEvent e);
}
