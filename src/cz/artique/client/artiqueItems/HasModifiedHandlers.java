package cz.artique.client.artiqueItems;

import com.google.gwt.event.shared.HandlerRegistration;

public interface HasModifiedHandlers {
	HandlerRegistration addGeneralClickHandler(ModifiedHandler handler);
}
