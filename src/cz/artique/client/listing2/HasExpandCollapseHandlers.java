package cz.artique.client.listing2;

import com.google.gwt.event.shared.HandlerRegistration;

public interface HasExpandCollapseHandlers {
	HandlerRegistration addExpandCollapseHandler(ExpandCollapseHandler handler);
}
