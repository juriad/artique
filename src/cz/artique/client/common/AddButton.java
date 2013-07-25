package cz.artique.client.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasOpenHandlers;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.InlineLabel;

/**
 * AddButton is button-like styled "plus sign" which fires {@link OpenEvent} on
 * click.
 * 
 * @author Adam Juraszek
 * 
 * @param <E>
 *            type of target of {@link OpenEvent}
 */
public class AddButton<E> extends InlineLabel
		implements HasOpenHandlers<E>, HasEnabled {

	public static final AddButtonFactory FACTORY = new AddButtonFactory();

	public static class AddButtonFactory {
		public <E> AddButton<E> createWidget(E whoFor) {
			return new AddButton<E>(whoFor);
		}
	}

	interface MyResources extends ClientBundle {
		@NotStrict
		@Source("AddButton.css")
		CssResource style();
	}

	private static final MyResources res = GWT.create(MyResources.class);

	protected static final String addSign = "+";

	private final E addFor;

	/**
	 * @param addFor
	 *            who will be target of {@link OpenEvent}
	 */
	public AddButton(E addFor) {
		super(addSign);
		res.style().ensureInjected();
		this.addFor = addFor;
		setStylePrimaryName("addButton");
		addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (isEnabled()) {
					OpenEvent.fire(AddButton.this, AddButton.this.addFor);
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

	public HandlerRegistration addOpenHandler(OpenHandler<E> handler) {
		return addHandler(handler, OpenEvent.getType());
	}

	/**
	 * @return who will be target of {@link OpenEvent}
	 */
	public E getAddFor() {
		return addFor;
	}
}
