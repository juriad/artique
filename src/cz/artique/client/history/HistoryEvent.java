package cz.artique.client.history;

import com.google.gwt.event.shared.GwtEvent;

public class HistoryEvent extends GwtEvent<HistoryHandler> {

	private static final Type<HistoryHandler> TYPE =
		new Type<HistoryHandler>();

	public static Type<HistoryHandler> getType() {
		return TYPE;
	}

	public HistoryEvent() {}

	@Override
	public final Type<HistoryHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(HistoryHandler handler) {
		handler.onHistoryChanged(this);
	}

}
