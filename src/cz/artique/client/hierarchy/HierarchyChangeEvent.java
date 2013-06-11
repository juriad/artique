package cz.artique.client.hierarchy;

import com.google.gwt.event.shared.GwtEvent;

import cz.artique.shared.utils.HasHierarchy;

public class HierarchyChangeEvent<E extends HasHierarchy>
		extends GwtEvent<HierarchyChangeHandler<E>> {

	private final Type<HierarchyChangeHandler<E>> TYPE =
		new Type<HierarchyChangeHandler<E>>();

	public <T extends HasHierarchy> Type<HierarchyChangeHandler<T>> getType() {
		return new Type<HierarchyChangeHandler<T>>();
	}

	private final Hierarchy<E> changed;
	private final HierarchyChangeType changeType;

	public HierarchyChangeEvent(Hierarchy<E> changed,
			HierarchyChangeType changeType) {
		this.changed = changed;
		this.changeType = changeType;
	}

	@Override
	public final Type<HierarchyChangeHandler<E>> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(HierarchyChangeHandler<E> handler) {
		handler.onHierarchyChange(this);
	}

	public Hierarchy<E> getChanged() {
		return changed;
	}

	public HierarchyChangeType getChangeType() {
		return changeType;
	}

}
