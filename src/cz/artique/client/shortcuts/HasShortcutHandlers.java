package cz.artique.client.shortcuts;

import com.google.gwt.event.shared.HandlerRegistration;

public interface HasShortcutHandlers {
	HandlerRegistration addShortcutHandler(ShortcutHandler handler);
}
