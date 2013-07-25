package cz.artique.client.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.InlineLabel;

/**
 * CloseButton is button-like styled letter X which fires {@link CloseEvent} on
 * click.
 * 
 * @author Adam Juraszek
 * 
 * @param <E>
 *            type of target of {@link CloseEvent}
 */
public class CloseButton<E> extends InlineLabel
		implements HasCloseHandlers<E>, HasEnabled {

	public static final CloseButtonFactory FACTORY = new CloseButtonFactory();

	public static class CloseButtonFactory {
		public <E> CloseButton<E> createWidget(E whoFor) {
			return new CloseButton<E>(whoFor);
		}
	}

	interface MyResources extends ClientBundle {
		@NotStrict
		@Source("CloseButton.css")
		CssResource style();
	}

	private static final MyResources res = GWT.create(MyResources.class);

	private static final String closeSign = "x";

	private final E closeFor;

	/**
	 * @param closeFor
	 *            who will be target of {@link CloseEvent}
	 */
	public CloseButton(E closeFor) {
		super(closeSign);
		res.style().ensureInjected();
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

	boolean enabled = true;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public HandlerRegistration addCloseHandler(CloseHandler<E> handler) {
		return addHandler(handler, CloseEvent.getType());
	}

	/**
	 * @return who will be target of {@link CloseEvent}
	 */
	public E getCloseFor() {
		return closeFor;
	}

}
