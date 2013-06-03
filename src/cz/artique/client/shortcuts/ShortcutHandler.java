package cz.artique.client.shortcuts;

import com.google.gwt.event.shared.EventHandler;

public interface ShortcutHandler extends EventHandler {
	void onShortcut(ShortcutEvent e);
}
