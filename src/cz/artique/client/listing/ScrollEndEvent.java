package cz.artique.client.listing;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Notifies about list being scrolled near or to the end.
 * 
 * @author Adam Juraszek
 * 
 */
public class ScrollEndEvent extends GwtEvent<ScrollEndHandler> {
	public enum ScrollEndType {
		/**
		 * Top reached.
		 */
		TOP,
		/**
		 * Scrollbar is near top.
		 */
		NEAR_TOP,
		/**
		 * Scrollbar is near bottom.
		 */
		NEAR_BOTTOM,
		/**
		 * Bottom reached.
		 */
		BOTTOM;
	}

	private static final Type<ScrollEndHandler> TYPE =
		new Type<ScrollEndHandler>();

	private final ScrollEndType scrollEndType;

	public static Type<ScrollEndHandler> getType() {
		return TYPE;
	}

	/**
	 * Creates a new event representing particular scrollbar position.
	 * 
	 * @param scrollEndType
	 *            scrollbar position
	 */
	public ScrollEndEvent(ScrollEndType scrollEndType) {
		this.scrollEndType = scrollEndType;
	}

	@Override
	public final Type<ScrollEndHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ScrollEndHandler handler) {
		handler.onScrollEnd(this);
	}

	/**
	 * @return type of scroll position whoch caused this event
	 */
	public ScrollEndType getScrollEndType() {
		return scrollEndType;
	}
}
