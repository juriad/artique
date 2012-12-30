package cz.artique.client.listing;

import com.google.gwt.event.shared.GwtEvent;

public class ScrollEndEvent extends GwtEvent<ScrollEndHandler> {
	public enum ScrollEndType {
		TOP,
		BOTTOM;
	}

	private static final Type<ScrollEndHandler> TYPE =
		new Type<ScrollEndHandler>();

	private final ScrollEndType scrollEndType;

	public static Type<ScrollEndHandler> getType() {
		return TYPE;
	}

	public ScrollEndEvent(ScrollEndType scrollEndType) {
		this.scrollEndType = scrollEndType;
	}

	@Override
	public final Type<ScrollEndHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ScrollEndHandler handler) {
		handler.onScroll(this);
	}

	public ScrollEndType getScrollEndType() {
		return scrollEndType;
	}
}
