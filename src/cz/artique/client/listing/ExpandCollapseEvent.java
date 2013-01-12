package cz.artique.client.listing;

import com.google.gwt.event.shared.GwtEvent;

public class ExpandCollapseEvent extends GwtEvent<ExpandCollapseHandler> {
	public enum ExpandCollapseType {
		EXPAND,
		COLLAPSE;
	}

	private static final Type<ExpandCollapseHandler> TYPE =
		new Type<ExpandCollapseHandler>();

	private final ExpandCollapseType expandCollapseType;

	public static Type<ExpandCollapseHandler> getType() {
		return TYPE;
	}

	public ExpandCollapseEvent(ExpandCollapseType expandCollapseType) {
		this.expandCollapseType = expandCollapseType;
	}

	@Override
	public final Type<ExpandCollapseHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ExpandCollapseHandler handler) {
		handler.onExpandOrCollapse(this);
	}

	public ExpandCollapseType getExpandCollapseType() {
		return expandCollapseType;
	}

}
