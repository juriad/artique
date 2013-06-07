package cz.artique.client.common;

import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.DialogBox;

import cz.artique.client.manager.Managers;

public class StopDialog extends DialogBox {

	private static class StopHandler
			implements KeyDownHandler, KeyUpHandler, KeyPressHandler {
		public void onKeyPress(KeyPressEvent event) {
			onEvent(event);
		}

		public void onKeyUp(KeyUpEvent event) {
			onEvent(event);
		}

		public void onKeyDown(KeyDownEvent event) {
			onEvent(event);
		}

		private void onEvent(KeyEvent<?> event) {
			if (Managers.SHORTCUTS_MANAGER.isColliding(event)) {
				event.stopPropagation();
			}
		}
	}

	private static StopHandler stop = new StopHandler();

	public StopDialog() {
		this(false);
	}

	public StopDialog(boolean autoHide, boolean modal, Caption captionWidget) {
		super(autoHide, modal, captionWidget);
		addDomHandler(stop, KeyDownEvent.getType());
		addDomHandler(stop, KeyUpEvent.getType());
		addDomHandler(stop, KeyPressEvent.getType());
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
