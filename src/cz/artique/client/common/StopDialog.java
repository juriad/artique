package cz.artique.client.common;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.DialogBox;

public class StopDialog extends DialogBox {

	public StopDialog() {
		this(false);
	}

	public StopDialog(boolean autoHide, boolean modal, Caption captionWidget) {
		super(autoHide, modal, captionWidget);
		addDomHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				event.stopPropagation();
			}
		}, KeyPressEvent.getType());
	}

	public StopDialog(boolean autoHide, boolean modal) {
		this(autoHide, modal, new CaptionImpl());
	}

	public StopDialog(boolean autoHide) {
		this(autoHide, true);
	}

	public StopDialog(Caption captionWidget) {
		this(false, true, captionWidget);
	}

}
