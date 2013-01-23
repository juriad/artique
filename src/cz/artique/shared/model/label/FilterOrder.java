package cz.artique.shared.model.label;

public enum FilterOrder {
	ASCENDING,
	DESCENDING;

	public static FilterOrder getDefault() {
		return DESCENDING;
	}
}
