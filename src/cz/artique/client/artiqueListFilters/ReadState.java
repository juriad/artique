package cz.artique.client.artiqueListFilters;

public enum ReadState {
	UNREAD(false),
	READ(true);

	private final boolean state;

	private ReadState(boolean state) {
		this.state = state;
	}

	public boolean isState() {
		return state;
	}

	public static ReadState get(Boolean read) {
		if (read == null) {
			return null;
		}
		return read ? READ : UNREAD;
	}
}
