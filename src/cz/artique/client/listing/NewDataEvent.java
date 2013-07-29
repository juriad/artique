package cz.artique.client.listing;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event represents fact that {@link InfiniteList} has new data available, or
 * has shown new data.
 * 
 * @author Adam Juraszek
 * 
 */
public class NewDataEvent extends GwtEvent<NewDataHandler> {
	private static final Type<NewDataHandler> TYPE = new Type<NewDataHandler>();

	public enum NewDataType {
		/**
		 * New data has been shown.
		 */
		NEW_DATA_SHOWN,
		/**
		 * New data are available to be shown.
		 */
		NEW_DATA_AVAILABLE;
	}

	public static Type<NewDataHandler> getType() {
		return TYPE;
	}

	private final NewDataType newDataType;

	private NewDataEvent(NewDataType newDataType) {
		this.newDataType = newDataType;
	}

	/**
	 * Fires the event.
	 * 
	 * @param source
	 *            source
	 * @param newDataType
	 *            type of the event
	 */
	public static <T> void fire(HasNewDataHandlers source,
			NewDataType newDataType) {
		if (TYPE != null) {
			NewDataEvent event = new NewDataEvent(newDataType);
			source.fireEvent(event);
		}
	}

	@Override
	public final Type<NewDataHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(NewDataHandler handler) {
		handler.onNewData(this);
	}

	/**
	 * @return type of the event
	 */
	public NewDataType getNewDataType() {
		return newDataType;
	}

}
