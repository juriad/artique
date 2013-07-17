package cz.artique.client.items;

import com.google.gwt.event.shared.HandlerRegistration;

public interface HasModifiedHandlers {
	HandlerRegistration addModifiedHandler(ModifiedHandler handler);
}
