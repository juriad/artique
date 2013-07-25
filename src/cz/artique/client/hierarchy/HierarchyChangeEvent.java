package cz.artique.client.hierarchy;

import com.google.gwt.event.shared.GwtEvent;

import cz.artique.shared.utils.HasHierarchy;

/**
 * Event describing what and in what manner changed.
 * 
 * @author Adam Juraszek
 * 
 * @param <E>
 *            type of hierarchy
 */
public class HierarchyChangeEvent<E extends HasHierarchy>
		extends GwtEvent<HierarchyChangeHandler<E>> {

	private final Type<HierarchyChangeHandler<E>> TYPE =
		new Type<HierarchyChangeHandler<E>>();

	public <T extends HasHierarchy> Type<HierarchyChangeHandler<T>> getType() {
		return new Type<HierarchyChangeHandler<T>>();
	}

	private final Hierarchy<E> changed;
	private final HierarchyChangeType changeType;

	/**
	 * @param changed
	 *            changed node
	 * @param changeType
	 *            type of change
	 */
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

	/**
	 * @return changed node
	 */
	public Hierarchy<E> getChanged() {
		return changed;
	}

	/**
	 * @return type of change
	 */
	public HierarchyChangeType getChangeType() {
		return changeType;
	}

}
