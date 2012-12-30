package cz.artique.client.listing;

import com.google.gwt.event.shared.HandlerRegistration;

public interface HasScrollEndHandlers {
	HandlerRegistration addScrollEndHandler(ScrollEndHandler handler);
}
