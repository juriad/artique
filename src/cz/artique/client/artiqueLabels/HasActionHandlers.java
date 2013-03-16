package cz.artique.client.artiqueLabels;

import com.google.gwt.event.shared.HandlerRegistration;

public interface HasActionHandlers {
	HandlerRegistration addActionHandler(ActionHandler handler);
}
