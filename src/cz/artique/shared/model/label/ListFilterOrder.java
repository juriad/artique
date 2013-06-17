package cz.artique.shared.model.label;

/**
 * Lists all possible values for ordering items in {@link ListFilter}.
 * 
 * @author Adam Juraszek
 * 
 */
public enum ListFilterOrder {
	/**
	 * Order items descending; newest first.
	 */
	DESCENDING,
	/**
	 * Order items ascending; oldest first.
	 */
	ASCENDING;

	/**
	 * @return default ordering, used when {@link ListFilterOrder} is null
	 */
	public static ListFilterOrder getDefault() {
		return DESCENDING;
	}
}
