package cz.artique.client.listing;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasScrollEndHandlers extends HasHandlers {
	HandlerRegistration addScrollEndHandler(ScrollEndHandler handler);
}
