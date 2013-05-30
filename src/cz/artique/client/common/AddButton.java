package cz.artique.client.common;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasOpenHandlers;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.InlineLabel;

public class AddButton<E> extends InlineLabel implements HasOpenHandlers<E> {

	public static final AddButtonFactory FACTORY = new AddButtonFactory();

	public static class AddButtonFactory {
		public <E> AddButton<E> createWidget(E whoFor) {
			return new AddButton<E>(whoFor);
		}
	}

	protected static final String addSign = "+";

	private final E addFor;

	public AddButton(E addFor) {
		super(addSign);
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

	boolean enabled;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public HandlerRegistration addOpenHandler(OpenHandler<E> handler) {
		return addHandler(handler, OpenEvent.getType());
	}

	public E getAddFor() {
		return addFor;
	}
}
