package cz.artique.client.listing;

import com.google.gwt.event.shared.EventHandler;

public interface ScrollEndHandler extends EventHandler {

	void onScroll(ScrollEndEvent event);
}