package cz.artique.client.listing;

import com.google.gwt.event.shared.GwtEvent;

public class NewDataEvent extends GwtEvent<NewDataHandler> {
	private static final Type<NewDataHandler> TYPE = new Type<NewDataHandler>();

	public enum NewDataType {
		NEW_DATA_SHOWN,
		NEW_DATA_AVAILABLE;
	}

	public static Type<NewDataHandler> getType() {
		return TYPE;
	}

	private final NewDataType newDataType;

	private NewDataEvent(NewDataType newDataType) {
		this.newDataType = newDataType;
	}

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

	public NewDataType getNewDataType() {
		return newDataType;
	}

}
