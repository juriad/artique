package cz.artique.client.artiqueLabels;

import com.google.gwt.event.shared.GwtEvent;

public class GeneralClickEvent extends GwtEvent<GeneralClickHandler> {

	private static final Type<GeneralClickHandler> TYPE =
		new Type<GeneralClickHandler>();

	public static Type<GeneralClickHandler> getType() {
		return TYPE;
	}

	public GeneralClickEvent() {}

	@Override
	public final Type<GeneralClickHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(GeneralClickHandler handler) {
		handler.onClick(this);
	}

}
