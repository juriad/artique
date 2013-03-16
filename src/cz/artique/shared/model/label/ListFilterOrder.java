package cz.artique.shared.model.label;

public enum ListFilterOrder {
	DESCENDING,
	ASCENDING;

	public static ListFilterOrder getDefault() {
		return DESCENDING;
	}
}
