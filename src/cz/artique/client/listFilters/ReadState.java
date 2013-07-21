package cz.artique.client.listFilters;

public enum ReadState {
	ALL(null),
	UNREAD(false),
	READ(true);

	private final Boolean state;

	private ReadState(Boolean state) {
		this.state = state;
	}

	public static ReadState get(Boolean read) {
		if (read == null) {
			return ALL;
		}
		return read ? READ : UNREAD;
	}

	public Boolean getState() {
		return state;
	}
}
