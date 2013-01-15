package cz.artique.client.labels;

import com.google.gwt.event.shared.GwtEvent;

public class RemoveEvent extends GwtEvent<RemoveHandler> {

	private static final Type<RemoveHandler> TYPE = new Type<RemoveHandler>();

	public static Type<RemoveHandler> getType() {
		return TYPE;
	}

	public RemoveEvent() {}

	@Override
	public final Type<RemoveHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(RemoveHandler handler) {
		handler.onRemove(this);
	}

}
