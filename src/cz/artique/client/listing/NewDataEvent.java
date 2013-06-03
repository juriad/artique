package cz.artique.client.listing;

import com.google.gwt.event.shared.GwtEvent;

public class NewDataEvent extends GwtEvent<NewDataHandler> {
	private static final Type<NewDataHandler> TYPE = new Type<NewDataHandler>();

	public static Type<NewDataHandler> getType() {
		return TYPE;
	}

	private NewDataEvent() {}

	public static <T> void fire(HasNewDataHandlers source) {
		if (TYPE != null) {
			NewDataEvent event = new NewDataEvent();
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

}
