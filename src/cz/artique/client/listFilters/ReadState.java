package cz.artique.client.listFilters;

/**
 * Read state shown in {@link ListFilterEditor}.
 * This represents all possible values of Boolean (true, false, null).
 * The selection is shown as a set of radio buttons.
 * 
 * @author Adam Juraszek
 * 
 */
public enum ReadState {
	/**
	 * Show all items.
	 */
	ALL(null),
	/**
	 * Show unread items only.
	 */
	UNREAD(false),
	/**
	 * Show read items only.
	 */
	READ(true);

	private final Boolean state;

	private ReadState(Boolean state) {
		this.state = state;
	}

	/**
	 * Converts Boolean into instance of this enum.
	 * 
	 * @param read
	 *            read state
	 * @return {@link ReadState}
	 */
	public static ReadState get(Boolean read) {
		if (read == null) {
			return ALL;
		}
		return read ? READ : UNREAD;
	}

	/**
	 * @return Boolean representation of state
	 */
	public Boolean getState() {
		return state;
	}
}
