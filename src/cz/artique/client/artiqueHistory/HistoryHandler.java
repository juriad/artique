package cz.artique.client.artiqueHistory;

import com.google.gwt.event.shared.EventHandler;

public interface HistoryHandler extends EventHandler {
	void onHistoryChanged(HistoryEvent e);
}
