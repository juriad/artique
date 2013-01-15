package cz.artique.client.labels;

import com.google.gwt.event.shared.HandlerRegistration;

public interface HasRemoveHandlers {
	HandlerRegistration addRemoveHandler(RemoveHandler handler);
}
