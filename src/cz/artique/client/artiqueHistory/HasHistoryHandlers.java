package cz.artique.client.artiqueHistory;

import com.google.gwt.event.shared.HandlerRegistration;

public interface HasHistoryHandlers {
	HandlerRegistration addHistoryHandler(HistoryHandler handler);

	HandlerRegistration addHistoryHandler(HistoryHandler handler, int priority);
}