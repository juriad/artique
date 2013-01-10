package cz.artique.client.listing2;

import com.google.gwt.event.shared.EventHandler;

public interface ExpandCollapseHandler extends EventHandler {
	void onExpandOrCollapse(ExpandCollapseEvent e);
}
