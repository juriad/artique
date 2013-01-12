package cz.artique.client.listing;

import com.google.gwt.event.shared.HandlerRegistration;

public interface HasExpandCollapseHandlers {
	HandlerRegistration addExpandCollapseHandler(ExpandCollapseHandler handler);
}
