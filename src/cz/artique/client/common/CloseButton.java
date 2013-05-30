package cz.artique.client.common;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.InlineLabel;

public class CloseButton<E> extends InlineLabel implements HasCloseHandlers<E> {
	
	public static final CloseButtonFactory FACTORY = new CloseButtonFactory();

	public static class CloseButtonFactory {
		public <E> CloseButton<E> createWidget(E whoFor) {
			return new CloseButton<E>(whoFor);
		}
	}

	private final static String closeSign = "x";

	private final E closeFor;

	public CloseButton(E closeFor) {
		super(closeSign);
		this.closeFor = closeFor;
		setStylePrimaryName("closeButton");
		addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (isEnabled()) {
					CloseEvent
						.fire(CloseButton.this, CloseButton.this.closeFor);
				}
			}
		});
	}

	boolean enabled;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public HandlerRegistration addCloseHandler(CloseHandler<E> handler) {
		return addHandler(handler, CloseEvent.getType());
	}

	public E getCloseFor() {
		return closeFor;
	}

}
