package cz.artique.client.items;

import com.google.gwt.event.shared.EventHandler;

public interface ModifiedHandler extends EventHandler {
	void onModified(ModifiedEvent e);
}
