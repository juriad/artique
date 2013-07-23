package cz.artique.client.listing;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasNewDataHandlers extends HasHandlers {
	HandlerRegistration addNewDataHandler(NewDataHandler handler);
}
