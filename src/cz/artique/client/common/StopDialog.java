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

/**
 * {@link DialogBox} which stops propagation of key events which collide with
 * shortcuts.
 * 
 * @author Adam Juraszek
 * 
 */
public class StopDialog extends DialogBox {

	/**
	 * Stop propagation if the key event can be interpreted as a shortcut.
	 * 
	 * @author Adam Juraszek
	 * 
	 */
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

	/**
	 * @see DialogBox#DialogBox()
	 */
	public StopDialog() {
		this(false);
	}

	/**
	 * @see DialogBox#DialogBox(boolean, boolean, Caption)
	 */
	public StopDialog(boolean autoHide, boolean modal, Caption captionWidget) {
		super(autoHide, modal, captionWidget);
		addDomHandler(stop, KeyDownEvent.getType());
		addDomHandler(stop, KeyUpEvent.getType());
		addDomHandler(stop, KeyPressEvent.getType());
	}

	/**
	 * @see DialogBox#DialogBox(boolean, boolean)
	 */
	public StopDialog(boolean autoHide, boolean modal) {
		this(autoHide, modal, new CaptionImpl());
	}

	/**
	 * @see DialogBox#DialogBox(boolean)
	 */
	public StopDialog(boolean autoHide) {
		this(autoHide, true);
	}

	/**
	 * @see DialogBox#DialogBox(Caption)
	 */
	public StopDialog(Caption captionWidget) {
		this(false, true, captionWidget);
	}

}
