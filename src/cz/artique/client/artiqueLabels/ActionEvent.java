package cz.artique.client.artiqueLabels;

import com.google.gwt.event.shared.GwtEvent;

public class ActionEvent extends GwtEvent<ActionHandler> {

	private static final Type<ActionHandler> TYPE = new Type<ActionHandler>();

	public static Type<ActionHandler> getType() {
		return TYPE;
	}

	public ActionEvent() {}

	@Override
	public final Type<ActionHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ActionHandler handler) {
		handler.onClick(this);
	}

}
