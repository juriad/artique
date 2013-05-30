package cz.artique.client.history;

import com.google.gwt.event.shared.EventHandler;

public interface HistoryHandler extends EventHandler {
	void onHistoryChanged(HistoryEvent e);
}
